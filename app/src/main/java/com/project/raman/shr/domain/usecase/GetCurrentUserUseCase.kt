package com.project.raman.shr.domain.usecase

import com.project.raman.shr.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getCurrentUser()
}
