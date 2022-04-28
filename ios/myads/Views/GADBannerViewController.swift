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
        let view = GADBannerView(adSize: GADAdSizeBanner)
        let viewController = UIViewController()
        view.adUnitID = "ca-app-pub-2898876693899601/8304589132"
        view.rootViewController = viewController
        viewController.view.addSubview(view)
        
        let frame = { () -> CGRect in
          // Here safe area is taken into account, hence the view frame is used
          // after the view has been laid out.
          if #available(iOS 11.0, *) {
            return view.frame.inset(by: view.safeAreaInsets)
          } else {
            return view.frame
          }
        }()
        let viewWidth = frame.size.width
        view.adSize = GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(viewWidth)
        
        view.load(GADRequest())
        return viewController
    }
 
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    
    }
}
