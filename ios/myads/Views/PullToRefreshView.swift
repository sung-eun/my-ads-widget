//
//  PullToRefreshView.swift
//  myads
//
//  Created by sung-eun on 2022/04/21.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import SwiftUI

struct PullToRefreshSwiftUI: View {
    @Binding private var needRefresh: Bool
    private let coordinateSpaceName: String
    private let onRefresh: () -> Void
    
    init(needRefresh: Binding<Bool>, coordinateSpaceName: String, onRefresh: @escaping () -> Void) {
        self._needRefresh = needRefresh
        self.coordinateSpaceName = coordinateSpaceName
        self.onRefresh = onRefresh
    }
    
    var body: some View {
        HStack(alignment: .center) {
            if needRefresh {
                VStack {
                    Spacer()
                    ProgressView()
                    Spacer()
                }
                .frame(height: 100)
            }
        }
        .background(GeometryReader {
            Color.clear.preference(key: ScrollViewOffsetPreferenceKey.self,
                                   value: $0.frame(in: .named(coordinateSpaceName)).origin.y)
        })
        .onPreferenceChange(ScrollViewOffsetPreferenceKey.self) { offset in
            guard !needRefresh else { return }
            if abs(offset) > 50 {
                needRefresh = true
                onRefresh()
            }
        }
    }
}


struct ScrollViewOffsetPreferenceKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }

}
