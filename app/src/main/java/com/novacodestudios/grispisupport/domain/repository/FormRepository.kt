package com.novacodestudios.grispisupport.domain.repository

import com.novacodestudios.grispisupport.presentation.detail.component.Form
import com.novacodestudios.grispisupport.presentation.detail.component.FormResponse

interface FormRepository {
    suspend fun getForms(): List<Form>
    suspend fun getForm(formId: String): Form?
    suspend fun getFormResponse(responseId: String): FormResponse?
}