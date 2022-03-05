//
//  SettingsView.swift
//  myads
//
//  Created by sung-eun on 2022/02/23.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import SwiftUI

struct SettingsView: View {
    var body: some View {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

private struct AnonymousProfileRow: View {
    let onConnectClick: () -> Void
    
    init(_ onConnectClick: @escaping () -> Void) {
        self.onConnectClick = onConnectClick
    }
    
    var body: some View {
        HStack(alignment: .center) {
            CircleResourceImage(image: UIImage(named: "IconGhost"), borderColor: Color("Gray200"))
            Spacer().frame(width: 12)
            Text(NSLocalizedString("connect_account", comment: ""))
                .font(.system(size: 16))
                .foregroundColor(Color("White"))
            Spacer().frame(width: 20)
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: 56,
            alignment: .leading
        )
        .onTapGesture(perform: onConnectClick)
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        AnonymousProfileRow({})
    }
}
