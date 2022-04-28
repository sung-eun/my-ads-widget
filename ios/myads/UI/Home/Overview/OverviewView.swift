//
//  OverviewView.swift
//  myads
//
//  Created by sung-eun on 2021/09/25.
//  Copyright © 2021 essie-cho. All rights reserved.
//

import SwiftUI
import AdSenseData

struct OverviewView: View {
    @EnvironmentObject var viewModel: HomeViewModel
    @State private var refresh: Bool = false
    
    var body: some View {
        ScrollView {
            Spacer().frame(height: 12)
            
            PullToRefreshSwiftUI(needRefresh: $refresh,
                                 coordinateSpaceName: "pullToRefresh")
            {
                DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                    withAnimation { refresh = false }
                }
                viewModel.refresh()
            }
            VStack {
                ErrorView(viewModel.error, viewModel.requestAdScopePermission)
                OverviewContent(viewModel.dashboardData)
            }
        }
        .background(Color("Black"))
        .accentColor(Color("Green500"))
    }
}

struct OverviewContent: View {
    let dashboardData: DashboardData
    
    init(_ dashboardData: DashboardData) {
        self.dashboardData = dashboardData
    }
    
    var body: some View {
        VStack {
            Spacer().frame(height: 8)
            OverviewCard(
                title: "balance",
                amountText: dashboardData.totalUnpaidAmount
            )
            Spacer().frame(height: 12)
            Text("last_7_days")
                .font(.system(size: 14, weight: .bold))
                .foregroundColor(Color("Green500"))
                .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      alignment: .leading
                )
            OverviewCard(
                title: "estimated_earnings",
                amountText: dashboardData.recentlyEstimatedIncome
            )
            Spacer().frame(height: 12)
            OverviewCard(
                title: "clicks",
                amountText: String(dashboardData.clicks)
            )
            Spacer().frame(height: 12)
            OverviewCard(
                title: "impressions",
                amountText: String(dashboardData.impressions)
            )
            Spacer().frame(height: 12)
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 16)
    }
}

private struct OverviewCard: View {
    let title: String
    let amountText: String
    
    init(title: String, amountText: String) {
        self.title = title
        self.amountText = amountText
    }
    
    var body: some View {
        VStack {
            Text(NSLocalizedString(title, comment: ""))
                .font(.system(size: 14, weight: .light))
                .foregroundColor(Color.white)
                .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      alignment: .leading
                    )
            Spacer().frame(height: 8)
            Text(amountText)
                .font(.system(size: 24, weight: .semibold))
                .foregroundColor(Color.white)
                .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      alignment: .leading
                )
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 10)
        .frame(
              minWidth: 0,
              maxWidth: .infinity,
              alignment: .topLeading
            )
        .background(RoundedRectangle(cornerRadius: 4)
                        .fill(Color("Background")))
    }
}

private struct ErrorView: View {
    let error: HomeErrorType?
    let requestAdScopePermission: () -> Void
    
    init(_ error: HomeErrorType?, _ requestAdScopePermission: @escaping () -> Void) {
        self.error = error
        self.requestAdScopePermission = requestAdScopePermission
    }
    
    var body: some View {
        if (error == HomeErrorType.ACCOUNT_NOT_CONNECTED) {
            Text("error_no_account")
                .font(.system(size: 14, weight: .light))
                .foregroundColor(Color("Background"))
                .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      alignment: .leading
                    )
            .frame(
                  minWidth: 0,
                  maxWidth: .infinity,
                  alignment: .topLeading
                )
            .padding(12)
            .background(Color("Yellow"))
        } else if (error == HomeErrorType.ADSENSE_NOT_PERMITTED) {
            Text("error_no_permission_read_adsense")
                .font(.system(size: 16))
                .foregroundColor(Color("Background"))
                .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      alignment: .leading
                    )
            .frame(
                  minWidth: 0,
                  maxWidth: .infinity,
                  alignment: .topLeading
                )
            .padding(12)
            .background(Color("Yellow"))
            .onTapGesture(perform: requestAdScopePermission)
        } else {
            EmptyView()
        }
    }
}

struct OverviewView_Previews: PreviewProvider {
    static var previews: some View {
//        OverviewCard(title: "title", amountText: "$0.00")
//        ErrorView(nil, {})
//        ScrollView {
//            ProgressView(value: 0, total: 1000)
//                .accentColor(Color("Green500"))
//            ErrorView(HomeErrorType.ADSENSE_NOT_PERMITTED, {})
//            OverviewContent(DashboardData(impressions: 33, clicks: 4, recentlyEstimatedIncome: "$10.2", dateRange: .last7days, totalUnpaidAmount: "$99.00"))
//        }
//        .background(Color("Black"))
//        OverviewContent(DashboardData(impressions: 33, clicks: 4, recentlyEstimatedIncome: "$10.2", dateRange: .last7days, totalUnpaidAmount: "$99.00"))
        Button(action: {
                    
                }) {
                    Text("Show Alert")
                }
                .alert(isPresented: .constant(true)) {
        Alert(title: Text("title_confirm_disconnect_account"), message: Text("message_confirm_disconnect_account"), primaryButton: .destructive(Text("label_ok"), action: {}), secondaryButton: .cancel(Text("label_cancel")))
                }
    }
}
