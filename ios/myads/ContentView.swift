import SwiftUI
import AdSenseData

struct ContentView: View {
    let viewModel: HomeViewModel
    
    init() {
        let googleAuthDelegate: IHttpHeaderProvider = GoogleAuthDelegate()
        let accountLocalDataSource: AdSenseLocalDataSource = LocalDataSourceDelegate().getAdSenseLocalDataSource()
        let accountRemoteDataSource: AdSenseRemoteDataSource = AdSenseRemoteDataSource(headerProvider: googleAuthDelegate)
        let accountRepository: IAccountRepository = AccountRepository(remoteDataSource: accountRemoteDataSource, localDataSource: accountLocalDataSource)
        
        let adsRepository: IAdsRepository = AdsRepository(dataSource: accountRemoteDataSource)
        
        let accountUseCase = AccountUseCase(accountRepository: accountRepository)
        let dashboardUseCase = DashboardDataUseCase(repository: adsRepository)
        
        UITabBar.appearance().unselectedItemTintColor = UIColor(Color("Gray200"))
        
        viewModel = HomeViewModel(accountUseCase, dashboardUseCase)
        viewModel.refresh()
    }
    
    var body: some View {
        VStack {
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
            
            GADBannerViewController()
                .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      minHeight: 0,
                      maxHeight: 60
                )
        }
    }
}
