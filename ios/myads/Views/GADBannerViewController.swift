//
//  GADBannerViewController.swift
//  myads
//
//  Created by sung-eun on 2022/04/26.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import GoogleMobileAds
import SwiftUI
import UIKit

struct GADBannerViewController: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        let view = GADBannerView(adSize: GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(UIScreen.main.bounds.size.width))
        let viewController = UIViewController()
        view.adUnitID = "ca-app-pub-2898876693899601/8304589132"
        view.rootViewController = viewController
        viewController.view.addSubview(view)
        view.load(GADRequest())
        return viewController
    }
 
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    
    }
}
