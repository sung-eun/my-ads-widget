//
//  HomeViewModel.swift
//  myads
//
//  Created by sung-eun on 2022/03/09.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import Foundation
import CommonDomain
import AdSenseData
import GoogleSignIn

class HomeViewModel: ObservableObject {
    @Published var loading = false
    @Published var loadingProgress: Double = 0
    @Published var error: HomeErrorType? = nil
    @Published var dashboardData: DashboardData = DashboardData(impressions: 0, clicks: 0, recentlyEstimatedIncome: "$0.00", dateRange: .last7days, totalUnpaidAmount: "$0.00")
    
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
            self.error = HomeErrorType.ACCOUNT_NOT_CONNECTED
        } else {
            self.googleUser = GIDSignIn.sharedInstance.currentUser
        }
    }
    
    func pullToRefresh() {
        
    }
    
    func requestAdScopePermission() {
        
    }
}
