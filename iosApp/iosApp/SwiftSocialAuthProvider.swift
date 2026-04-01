import SwiftUI
import ComposeApp
import FirebaseCore
import GoogleSignIn

class SwiftSocialAuthProvider: NSObject, SocialAuthManagerDelegate {

    func googleSignIn() async throws -> SocialAuthResult {
        
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            return SocialAuthResult.Failure(
                error: AppErrorCode.socialAuthConfigMissing
            )
        }
    

        let config = GIDConfiguration(clientID: clientID)
        GIDSignIn.sharedInstance.configuration = config

        guard let scene = await UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let rootViewController = await scene.windows.first?.rootViewController else {
            return SocialAuthResult.Failure(
                error: AppErrorCode.socialAuthConfigMissing
            )
        }

        return try await withCheckedThrowingContinuation { continuation in
            GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { result, error in

                if let error = error {
                    let nsError = error as NSError
                    
                    if nsError.domain == kGIDSignInErrorDomain,
                       nsError.code == GIDSignInError.Code.canceled.rawValue {
                        continuation.resume(returning: SocialAuthResult.Cancelled())
                    } else {
                        continuation.resume(returning: SocialAuthResult.Failure(
                            error: AppErrorCode.socialAuthFailed
                        ))
                    }
                    return
                }

                guard let user = result?.user,
                      let idToken = user.idToken?.tokenString else {
                    continuation.resume(returning: SocialAuthResult.Failure(
                        error: AppErrorCode.socialAuthFailed
                    ))
                    return
                }

                continuation.resume(returning: SocialAuthResult.Success(
                    idToken: idToken,
                    accessToken: user.accessToken.tokenString
                ))
            }
        }
    }

    func appleSignIn() async throws -> SocialAuthResult {
        return SocialAuthResult.Failure(
            error: AppErrorCode.socialAuthFailed
        )
    }

    func facebookSignIn() async throws -> SocialAuthResult {
        return SocialAuthResult.Failure(
            error: AppErrorCode.socialAuthFailed
        )
    }

    func signOut() async throws {
        GIDSignIn.sharedInstance.signOut()
    }
}
