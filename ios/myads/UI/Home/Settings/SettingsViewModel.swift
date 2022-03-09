//
//  SettingsViewModel.swift
//  myads
//
//  Created by 조성은 on 2022/02/23.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import Foundation
import CommonDomain
import AdSenseData
import GoogleSignIn
import SwiftUI

class SettingsViewModel: ObservableObject {
    @Published var googleUser: GIDGoogleUser? = nil
    @Published var selectedAdAccountName: String = ""
    @Published var adAccounts: [AdAccount] = []
    
    init() {
        check()
    }
    
    private func check() {
        GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
            self.checkStatus()
        }
    }
    
    func connectGoogle() {
        guard let presentingViewController = (UIApplication.shared.connectedScenes.first as? UIWindowScene)?.windows.first?.rootViewController else {return}

                let signInConfig = GIDConfiguration.init(
                    clientID: "678911608289-ig1cb4n588h99v5d8lvok0qnncrprbnq.apps.googleusercontent.com",
                    serverClientID: ServerConfig.init().CLIENT_ID)
                GIDSignIn.sharedInstance.signIn(
                    with: signInConfig,
                    presenting: presentingViewController,
                    callback: { user, error in
                        self.checkStatus()
                    }
                )
    }
    
    func disconnectGoogle() {
        GIDSignIn.sharedInstance.disconnect()
        self.checkStatus()
    }
    
    private func checkStatus() {
        if (GIDSignIn.sharedInstance.currentUser == nil) {
            self.googleUser = nil
        } else {
            self.googleUser = GIDSignIn.sharedInstance.currentUser
        }
    }
}
