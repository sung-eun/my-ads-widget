//
//  HomeViewModel.swift
//  myads
//
//  Created by sung-eun on 2022/03/09.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import Foundation
import AdSenseData
import GoogleSignIn

class HomeViewModel: ObservableObject {
    let adSenseReadScope = "https://www.googleapis.com/auth/adsense.readonly"
    
    let accountUseCase: AccountUseCase
    let dashboardDataUseCase: DashboardDataUseCase
    
    @Published var loading = false
    @Published var loadingProgress: Double = 0
    @Published var error: HomeErrorType? = nil
    @Published var dashboardData: DashboardData = DashboardData(impressions: 0, clicks: 0, recentlyEstimatedIncome: "$0.00", dateRange: .last7days, totalUnpaidAmount: "$0.00")
    
    @Published var googleUser: GIDGoogleUser? = nil
    @Published var selectedAdAccountName: String = ""
    @Published var adAccounts: [AdAccount] = []
    
    init(_ accountUseCase: AccountUseCase, _ dashboardDataUseCase: DashboardDataUseCase) {
        self.accountUseCase = accountUseCase
        self.dashboardDataUseCase = dashboardDataUseCase
        check()
    }
    
    private func check() {
        GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
            self.checkStatus()
        }
    }
    
    func connectGoogle() {
        guard let presentingViewController = (UIApplication.shared.connectedScenes.first as? UIWindowScene)?.windows.first?.rootViewController else { return }

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
            if (!hasAdSenseReadPermission()) {
                self.error = HomeErrorType.ADSENSE_NOT_PERMITTED
            } else {
                authenticate { authentication, error in
                    guard error == nil else { return }
                    guard authentication != nil else { return }
                    self.refresh()
                }
            }
        }
    }
    
    private func hasAdSenseReadPermission() -> Bool {
        if (self.googleUser == nil) {
            return false
        }
        let grantedScopes = self.googleUser?.grantedScopes
        return grantedScopes != nil && grantedScopes!.contains(adSenseReadScope)
    }
    
    func pullToRefresh() {
        refresh()
    }
    
    func requestAdScopePermission() {
        guard let presentingViewController = (UIApplication.shared.connectedScenes.first as? UIWindowScene)?.windows.first?.rootViewController else { return }
        
        let additionalScopes = [adSenseReadScope]
        GIDSignIn.sharedInstance.addScopes(additionalScopes, presenting: presentingViewController) { user, error in
            self.checkStatus()
        }
    }
    
    func refresh() {
        self.loading = true
        accountUseCase.getSelectedAccountId() { selectedId, error in
            if (selectedId == nil || selectedId!.isEmpty) {
                if (self.googleUser == nil) {
                    self.error = HomeErrorType.ACCOUNT_NOT_CONNECTED
                } else if (!self.hasAdSenseReadPermission()) {
                    self.error = HomeErrorType.ADSENSE_NOT_PERMITTED
                }
                return
            }
            self.error = nil
            self.dashboardDataUseCase.getDashboardData(accountName: selectedId!, dateRange: DateRange.last7days) { dashboardData, error in
                self.loading = false
                guard let dashboardData = dashboardData else { return }
                self.dashboardData = dashboardData
            }
        }
    }
    
    private func authenticate(authAction: @escaping GIDAuthenticationAction) {
        guard let googleUser = GIDSignIn.sharedInstance.currentUser else { return }
        googleUser.authentication.do(freshTokens: authAction)
    }
}
