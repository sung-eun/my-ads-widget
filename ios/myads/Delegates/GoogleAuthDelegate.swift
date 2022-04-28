//
//  GoogleAuthDelegate.swift
//  myads
//
//  Created by sung-eun on 2022/04/25.
//  Copyright © 2022 essie-cho. All rights reserved.
//

import Foundation
import AdSenseData
import GoogleSignIn

class GoogleAuthDelegate: IHttpHeaderProvider {
    
     func getAccessToken() -> String {
         guard let googleUser = GIDSignIn.sharedInstance.currentUser else { return "" }
         return googleUser.authentication.accessToken
    }
}
