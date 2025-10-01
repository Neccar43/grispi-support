package com.novacodestudios.grispisupport.data.repository

import com.novacodestudios.grispisupport.domain.repository.FormRepository
import com.novacodestudios.grispisupport.presentation.detail.component.Form
import com.novacodestudios.grispisupport.presentation.detail.component.FormResponse
import com.novacodestudios.grispisupport.presentation.util.DummyDataSource
import javax.inject.Inject

class FakeFormRepository @Inject constructor() : FormRepository {
    private val forms = DummyDataSource.forms

    private val formResponses = DummyDataSource.formResponses

    override suspend fun getForms(): List<Form> {
        return forms
    }

    override suspend fun getForm(formId: String): Form? {
        return forms.find { it.id == formId }
    }

    override suspend fun getFormResponse(responseId: String): FormResponse? {
        return formResponses.find { it.id == responseId }
    }
}