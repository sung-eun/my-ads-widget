//
//  SettingsView.swift
//  myads
//
//  Created by sung-eun on 2022/02/23.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import SwiftUI
import GoogleSignIn
import AdSenseData

struct SettingsView: View {
    @EnvironmentObject var viewModel: HomeViewModel
    
    var body: some View {
        VStack(alignment: .leading) {
            AccountSettingContent(
                onConnectClick: { viewModel.connectGoogle() },
                googleUser: viewModel.googleUser,
                onDisconnectClick: { viewModel.disconnectGoogle() })
            if (viewModel.googleUser != nil) {
                AdAccountSettingContent(
                    selectedAccountName: viewModel.selectedAdAccountName,
                    adAccounts: viewModel.adAccounts,
                    onSelectItem: { account in
                        viewModel.selectAdAccount(account)
                    }
                    )
            }
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
            Text("connect_account")
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
    
    @State private var showingAlert: Bool = false
    
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
                Button(role: .destructive, action: {
                    self.showingAlert = true
                }) {
                    Image(systemName: "xmark")
                        .foregroundColor(Color("Error"))
                        .frame(width: 30, height: 30, alignment: .center)
                }
                .alert(isPresented: $showingAlert) {
                    Alert(title: Text("message_confirm_disconnect_account"), primaryButton: .destructive(Text("label_ok"), action: onDisconnectClicked), secondaryButton: .cancel(Text("label_cancel")))
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

private struct AdAccountSettingContent: View {
    let selectedAccountName: String
    let adAccounts: [AdAccount]
    let onSelectItem: (AdAccount) -> Void
    
    @State var expanded: Bool = false
    
    init(selectedAccountName: String,
         adAccounts: [AdAccount],
         onSelectItem: @escaping (AdAccount) -> Void
    ) {
        self.selectedAccountName = selectedAccountName
        self.adAccounts = adAccounts
        self.onSelectItem = onSelectItem
    }
    
    var body: some View {
        VStack {
            HStack(alignment: .center) {
                Text(selectedAccountName)
                    .font(.system(size: 16))
                    .foregroundColor(Color("Black"))
                    .truncationMode(.middle)
                Spacer().frame(width: 8)
                HStack(alignment: .center) {
                    if (expanded) {
                        Image(systemName: "chevron.up")
                            .foregroundColor(Color("Black"))
                            .frame(width: 5, height: 5, alignment: .center)
                    } else {
                        Image(systemName: "chevron.down")
                            .foregroundColor(Color("Black"))
                            .frame(width: 5, height: 5, alignment: .center)
                    }
                }
                .frame(
                    minWidth: 0,
                    maxWidth: .infinity,
                    alignment: .trailing
                )
            }
            .padding(12)
            .frame(
                minWidth: 0,
                maxWidth: .infinity,
                alignment: .leading
            )
            .onTapGesture{
                self.expanded.toggle()
            }
            
            if (expanded) {
                ForEach(adAccounts, id: \.self) { item in
                    Text(item.id).onTapGesture {
                        onSelectItem(item)
                        self.expanded.toggle()
                    }
                    .font(.system(size: 16))
                    .foregroundColor(Color("Black"))
                    .padding(12)
                }
            }
        }
        .background(Color("Gray500"))
        .cornerRadius(5)
    }
    
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        HStack {
            VStack(alignment: .leading) {
                Text("selectedAccountName")
                    .font(.system(size: 16))
                    .lineLimit(1)
                    .foregroundColor(Color("Black"))
            }
            VStack(alignment: HorizontalAlignment.trailing) {
                Image(systemName: "chevron.down")
                    .frame(width: 10, height: 10, alignment: .center)
            }
            .frame(
                minWidth: 0,
                maxWidth: .infinity
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
