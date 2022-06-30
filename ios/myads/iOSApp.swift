import SwiftUI
import Firebase
import GoogleMobileAds
import AppTrackingTransparency
import AdSupport

@main
struct iOSApp: App {
    
    init() {
        FirebaseApp.configure()
        GADMobileAds.sharedInstance().start(completionHandler: nil)
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onReceive(NotificationCenter.default.publisher(for: UIApplication.didBecomeActiveNotification)) { _ in
                    ATTrackingManager.requestTrackingAuthorization(completionHandler: { status in })
                }
        }
    }
}
