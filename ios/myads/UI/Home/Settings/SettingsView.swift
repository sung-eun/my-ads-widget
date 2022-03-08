//
//  SettingsView.swift
//  myads
//
//  Created by sung-eun on 2022/02/23.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import SwiftUI
import GoogleSignIn

struct SettingsView: View {
    var body: some View {
        VStack(alignment: .leading) {
            AccountSettingContent(onConnectClick: {}, googleUser: nil, onDisconnectClick: {})
        }
        .padding(16)
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .topLeading
        )
        .background(Color("Black"))
    }
}

private struct AccountSettingContent: View {
    let onConnectClick: () -> Void
    let googleUser: GIDGoogleUser!
    let onDisconnectClick: () -> Void
    
    init(onConnectClick: @escaping () -> Void,
         googleUser: GIDGoogleUser!,
         onDisconnectClick: @escaping () -> Void) {
        self.onConnectClick = onConnectClick
        self.googleUser = googleUser
        self.onDisconnectClick = onDisconnectClick
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("account")
                .font(.system(size: 14))
                .bold()
                .foregroundColor(Color("White"))
            if (googleUser == nil) {
                AnonymousProfileRow(onConnectClick)
            } else {
                UserProfileRow(googleUser, onDisconnectClick)
            }
        }
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
                .lineLimit(1)
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

private struct UserProfileRow: View {
    let googleUser: GIDGoogleUser
    let onDisconnectClicked: () -> Void
    
    init(_ googleUser: GIDGoogleUser, _ onDisconnectClicked: @escaping () -> Void) {
        self.googleUser = googleUser
        self.onDisconnectClicked = onDisconnectClicked
    }
    
    var body: some View {
        HStack(alignment: .center) {
            CircleRemoteImage(imageUrl: googleUser.profile?.imageURL(withDimension: 320)?.absoluteString, borderColor: Color("Green200"))
            Spacer().frame(width: 12)
            Text(googleUser.profile?.name ?? NSLocalizedString("unknown", comment: ""))
                .font(.system(size: 16))
                .foregroundColor(Color("White"))
                .lineLimit(1)
            Spacer().frame(width: 20)
            HStack(alignment: .center) {
                Button(role: .destructive, action: onDisconnectClicked) {
                    Image(systemName: "xmark")
                        .foregroundColor(Color("Error"))
                        .frame(width: 30, height: 30, alignment: .center)
                }
            }
            .frame(
                minWidth: 0,
                maxWidth: .infinity,
                alignment: .trailing
            )
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: 56,
            alignment: .leading
        )
    }
}

private struct Test: View {
    var body: some View {
        HStack(alignment: .center) {
            CircleRemoteImage(imageUrl: "https://www.fujifilm.com/products/digital_cameras/x/fujifilm_x_t1/sample_images/img/index/pic_01.jpg", borderColor: Color("Green200"))
            Spacer().frame(width: 12)
            Text("display")
//            Text("display" ?? NSLocalizedString("unknown", comment: ""))
                .font(.system(size: 16))
                .foregroundColor(Color("White"))
                .lineLimit(1)
            Spacer().frame(width: 20)
            HStack(alignment: .center) {
                Button(role: .destructive, action: {}) {
                    Image(systemName: "xmark")
                        .foregroundColor(Color("Error"))
                        .frame(width: 30, height: 30, alignment: .center)
                }
            }
            .frame(
                minWidth: 0,
                maxWidth: .infinity,
                alignment: .trailing
            )
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: 56,
            alignment: .leading
        )
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}
