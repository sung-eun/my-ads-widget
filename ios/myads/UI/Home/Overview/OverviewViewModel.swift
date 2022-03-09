//
//  MainViewModel.swift
//  myads
//
//  Created by sung-eun on 2022/02/23.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import Foundation
import CommonDomain

class OverviewViewModel: ObservableObject {
    @Published var loading = false
    @Published var loadingProgress: Double = 0
    @Published var error: HomeErrorType? = nil
    @Published var dashboardData: DashboardData = DashboardData(impressions: 0, clicks: 0, recentlyEstimatedIncome: "$0.00", dateRange: .last7days, totalUnpaidAmount: "$0.00")
    
    func pullToRefresh() {
        
    }
}
