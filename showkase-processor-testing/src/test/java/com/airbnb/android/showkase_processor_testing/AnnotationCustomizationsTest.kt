package com.airbnb.android.showkase_processor_testing

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import org.junit.Test

class AnnotationCustomizationsTest: BaseProcessorTest() {

    @Test
    fun `custom annotation with standard fields`() {
        val options = mutableMapOf<String, String>()
        options[ShowkaseComposable.AnnotationOverride] = "ShowkaseProcessorTest.custom_annotation_with_standard_fields.input.CustomAnnotation"
        compileInputsAndVerifyOutputs(options = options)
    }

    @Test
    fun `custom annotation with custom fields`() {
        val options = mutableMapOf<String, String>()
        options[ShowkaseComposable.AnnotationOverride] = "ShowkaseProcessorTest.custom_annotation_with_custom_fields.input.CustomAnnotation"
        options[ShowkaseComposable.FieldOverrideName] = "label"
        options[ShowkaseComposable.FieldOverrideGroup] = "category"
        options[ShowkaseComposable.FieldOverrideStyleName] = "style"
        // This is only supported in KSP at the moment
        compileInputsAndVerifyOutputs(options = options, modes = listOf(Mode.KSP))
    }

    @Test
    fun `custom annotation with multiple field names`() {
        val options = mutableMapOf<String, String>()
        options[ShowkaseComposable.AnnotationOverride] = "ShowkaseProcessorTest.custom_annotation_with_multiple_field_names.input.CustomAnnotation"
        options[ShowkaseComposable.FieldOverrideName] = "label|secondLabel"
        options[ShowkaseComposable.FieldOverrideGroup] = "category|secondCategory"
        options[ShowkaseComposable.FieldOverrideStyleName] = "style|secondStyle"
        // This is only supported in KSP at the moment
        compileInputsAndVerifyOutputs(options = options, modes = listOf(Mode.KSP))
    }

    @Test
    fun `custom annotation missing`() {
        val options = mutableMapOf<String, String>()
        options[ShowkaseComposable.AnnotationOverride] = "ShowkaseProcessorTest.custom_annotation_with_standard_fields.input.WrongAnnotation"
        compileInputsAndVerifyOutputs(options = options)
    }

    @Test
    fun `custom annotation with missing fields`() {
        val options = mutableMapOf<String, String>()
        options[ShowkaseComposable.AnnotationOverride] = "ShowkaseProcessorTest.custom_annotation_with_missing_fields.input.CustomAnnotation"
        options[ShowkaseComposable.FieldOverrideName] = "label"
        options[ShowkaseComposable.FieldOverrideGroup] = "category"
        options[ShowkaseComposable.FieldOverrideStyleName] = "style"
        // This is only supported in KSP at the moment
        assertCompilationFails(
            options = options,
            modes = listOf(Mode.KSP),
            errorMessage = "No property named skip was found in annotation CustomAnnotation")
    }
}