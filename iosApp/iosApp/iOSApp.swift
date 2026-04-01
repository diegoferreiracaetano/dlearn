import SwiftUI
import ComposeApp
import FirebaseCore

@main
struct iOSApp: App {
    
    init() {
        FirebaseApp.configure()
        
        // Configura o delegate para a implementação nativa de autenticação social
        SocialAuthManager.companion.delegate = SwiftSocialAuthProvider()
        
        // Inicializa o Koin (Common) - Agora sem precisar de parâmetros de plataforma
        AppModuleKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
