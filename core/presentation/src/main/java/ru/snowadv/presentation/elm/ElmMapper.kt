package ru.snowadv.presentation.elm

interface ElmMapper<PresentationState, PresentationEffect, PresentationEvent, UiState, UiEffect, UiEvent> {
    fun mapState(state: PresentationState): UiState
    fun mapEffect(effect: PresentationEffect): UiEffect
    fun mapUiEvent(uiEvent: UiEvent): PresentationEvent
}