import SwiftUI

struct ContentView: View {
    let viewModel: HomeViewModel = HomeViewModel()
    
    init() {
        UITabBar.appearance().unselectedItemTintColor = UIColor(Color("Gray200"))
    }
    
    var body: some View {
        TabView {
            OverviewView().environmentObject(viewModel)
                .tabItem {
                    Image(systemName: "chart.pie")
                    Text("OVERVIEW")
                }
            
            SettingsView().environmentObject(viewModel)
                .tabItem {
                    Image(systemName: "person.fill")
                    Text("SETTINGS")
                }
        }
        .accentColor(Color("Green200"))
    }
}
