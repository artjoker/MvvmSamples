package ua.artjoker.mvvmsamples.viewmodels

import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.core.Profile
import ua.artjoker.mvvmsamples.core.SignInInteractor
import ua.artjoker.mvvmsamples.ui.clear
import ua.artjoker.mvvmsamples.viewmodels.bases.EmailViewModel
import ua.artjoker.mvvmsamples.viewmodels.bases.FormViewModel
import ua.artjoker.mvvmsamples.viewmodels.bases.PasswordViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthViewModel @Inject constructor(
        private val interactor: SignInInteractor) : FormViewModel<Profile>() {

    val email = EmailViewModel()
    val password = PasswordViewModel()

    override val fields = listOf(email, password)

    override fun onSend() = interactor.auth(email.value(), password.value())

    override fun onFormSent() {
        appFacade.showToast(R.string.authorization_successful)
        router.exit()
    }

    override fun onSendingFailed(e: Throwable) {
        when (e) {
            SignInInteractor.UnknownEmailException -> email.showVerificationError(R.string.error_incorrect_email)
            SignInInteractor.WrongPasswordException -> password.showVerificationError(R.string.error_incorrect_password)
        }
    }
}
