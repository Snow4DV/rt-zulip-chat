package ru.snowadv.chat_presentation.chat.presentation.elm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.chat_presentation.chat.presentation.elm.data.ChatElmUiMapperTestData
import ru.snowadv.chat_presentation.chat.presentation.elm.data.ChatReducerElmTestData
import ru.snowadv.chat_presentation.chat.presentation.model.SnackbarText
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEffectUiElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatElmUiMapper
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEventUiElm
import ru.snowadv.chat_presentation.chat.ui.model.SnackbarUiText
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState
import ru.snowadv.test_utils.exception.DataException

@RunWith(JUnit4::class)
class ChatElmUiMapperTest : BehaviorSpec({
    with(ChatElmUiMapperTestData()) {
        Given("ChatElmUiMapper") {
            val mapper = ChatElmUiMapper()
            When("map state") {
                And("state is initial (resumed)") {
                    val mappedState = mapper.mapState(initialResumedState)
                    Then("mappedState should be initialResumedStateMapped") {
                        mappedState shouldBe initialResumedStateMapped
                    }
                }
                And("state is first page loaded, eventQueueData is null and paginationStatus is HasMore") {
                    val mappedState = mapper.mapState(firstPageStateNullEventQueueData)
                    Then("mappedState should be firstPageStateNullEventQueueDataMapped") {
                        mappedState shouldBe firstPageStateNullEventQueueDataMapped
                    }
                }
                And("state is first page loaded, eventQueueData is not null and paginationStatus is HasMore") {
                    val mappedState = mapper.mapState(firstPageStateObserveStarted)
                    Then("mappedState should be firstPageStateObserveStartedMapped") {
                        mappedState shouldBe firstPageStateObserveStartedMapped
                    }
                }
            }

            When("map effect to ui") {
                And("effect is OpenFileChooser") {
                    val effect = ChatEffectElm.OpenFileChooser
                    val expectedEffect = ChatEffectUiElm.OpenFileChooser

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }
                And("effect is OpenFileChooser") {
                    val effect = ChatEffectElm.OpenReactionChooser(1, listOf("1", "2"))
                    val expectedEffect = ChatEffectUiElm.OpenReactionChooser(1, listOf("1", "2"))

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }
                And("effect is OpenReactionChooser") {
                    val effect = ChatEffectElm.OpenReactionChooser(1, listOf("1", "2"))
                    val expectedEffect = ChatEffectUiElm.OpenReactionChooser(1, listOf("1", "2"))

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }
                And("effect is ShowActionErrorWithRetry") {
                    val effect = ChatEffectElm.ShowActionErrorWithRetry(ChatEventElm.Ui.Init)
                    val expectedEffect = ChatEffectUiElm.ShowActionErrorWithRetry(ChatEventElm.Ui.Init)

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }

                And("effect is ShowSnackbarWithText") {
                    val effect = ChatEffectElm.ShowSnackbarWithText(SnackbarText.ACTION_ERROR)
                    val expectedEffect = ChatEffectUiElm.ShowSnackbarWithText(SnackbarUiText.ACTION_ERROR)

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }

                And("effect is OpenMessageActionsChooser") {
                    val effect = ChatEffectElm.OpenMessageActionsChooser(1, 2, "stream", true)
                    val expectedEffect = ChatEffectUiElm.OpenMessageActionsChooser(1, 2, "stream", true)

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }

                And("effect is ShowTopicChangedBecauseNewMessageIsUnreachable") {
                    val effect = ChatEffectElm.ShowTopicChangedBecauseNewMessageIsUnreachable
                    val expectedEffect = ChatEffectUiElm.ShowTopicChangedBecauseNewMessageIsUnreachable

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }

                And("effect is OpenMessageEditor") {
                    val effect = ChatEffectElm.OpenMessageEditor(1, "stream")
                    val expectedEffect = ChatEffectUiElm.OpenMessageEditor(1, "stream")

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }

                And("effect is OpenMessageTopicChanger") {
                    val effect = ChatEffectElm.OpenMessageTopicChanger(1, 2, "topic")
                    val expectedEffect = ChatEffectUiElm.OpenMessageTopicChanger(1, 2, "topic")

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }

                And("effect is RefreshMessageWithId") {
                    val effect = ChatEffectElm.RefreshMessageWithId(1)
                    val expectedEffect = ChatEffectUiElm.RefreshMessageWithId(1)

                    val mappedEffect = mapper.mapEffect(effect)
                    Then("mapped effect should be $expectedEffect") {
                        mappedEffect shouldBe expectedEffect
                    }
                }
            }

            When("map UiElmEvent to ElmEvent") {
                And("Ui event is Init") {
                    val uiEvent = ChatEventUiElm.Init
                    val expectedMappedEvent = ChatEventElm.Ui.Init

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }
                And("Ui event is Resumed") {
                    val uiEvent = ChatEventUiElm.Resumed
                    val expectedMappedEvent = ChatEventElm.Ui.Resumed

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is Paused") {
                    val uiEvent = ChatEventUiElm.Paused
                    val expectedMappedEvent = ChatEventElm.Ui.Paused

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is SendMessageAddAttachmentButtonClicked") {
                    val uiEvent = ChatEventUiElm.SendMessageAddAttachmentButtonClicked
                    val expectedMappedEvent = ChatEventElm.Ui.SendMessageAddAttachmentButtonClicked

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is MessageLongClicked") {
                    val uiEvent = ChatEventUiElm.MessageLongClicked(1, 2)
                    val expectedMappedEvent = ChatEventElm.Ui.MessageLongClicked(1, 2)

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is AddReactionClicked") {
                    val uiEvent = ChatEventUiElm.AddReactionClicked(1)
                    val expectedMappedEvent = ChatEventElm.Ui.AddReactionClicked(1)

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is AddChosenReaction") {
                    val uiEvent = ChatEventUiElm.AddChosenReaction(1, "reaction")
                    val expectedMappedEvent = ChatEventElm.Ui.AddChosenReaction(1, "reaction")

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is RemoveReaction") {
                    val uiEvent = ChatEventUiElm.RemoveReaction(1, "reaction")
                    val expectedMappedEvent = ChatEventElm.Ui.RemoveReaction(1, "reaction")

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is MessageFieldChanged") {
                    val uiEvent = ChatEventUiElm.MessageFieldChanged("text")
                    val expectedMappedEvent = ChatEventElm.Ui.MessageFieldChanged("text")

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is GoBackClicked") {
                    val uiEvent = ChatEventUiElm.GoBackClicked
                    val expectedMappedEvent = ChatEventElm.Ui.GoBackClicked

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is ReloadClicked") {
                    val uiEvent = ChatEventUiElm.ReloadClicked
                    val expectedMappedEvent = ChatEventElm.Ui.ReloadClicked

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is PaginationLoadMore") {
                    val uiEvent = ChatEventUiElm.PaginationLoadMore
                    val expectedMappedEvent = ChatEventElm.Ui.PaginationLoadMore

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is ScrolledToNTopMessages") {
                    val uiEvent = ChatEventUiElm.ScrolledToNTopMessages
                    val expectedMappedEvent = ChatEventElm.Ui.ScrolledToNTopMessages

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is FileChoosingDismissed") {
                    val uiEvent = ChatEventUiElm.FileChoosingDismissed
                    val expectedMappedEvent = ChatEventElm.Ui.FileChoosingDismissed

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is FileWasChosen") {
                    val opener = InputStreamOpener {null}
                    val uiEvent = ChatEventUiElm.FileWasChosen("image/png", opener, "png")
                    val expectedMappedEvent = ChatEventElm.Ui.FileWasChosen("image/png", opener, "png")

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is ClickedOnTopic") {
                    val uiEvent = ChatEventUiElm.ClickedOnTopic("topic")
                    val expectedMappedEvent = ChatEventElm.Ui.ClickedOnTopic("topic")

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is OnLeaveTopicClicked") {
                    val uiEvent = ChatEventUiElm.OnLeaveTopicClicked
                    val expectedMappedEvent = ChatEventElm.Ui.ClickedOnLeaveTopic

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is TopicChanged") {
                    val uiEvent = ChatEventUiElm.TopicChanged("new topic")
                    val expectedMappedEvent = ChatEventElm.Ui.TopicChanged("new topic")

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is EditMessageClicked") {
                    val uiEvent = ChatEventUiElm.EditMessageClicked(1)
                    val expectedMappedEvent = ChatEventElm.Ui.EditMessageClicked(1)

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is MoveMessageClicked") {
                    val uiEvent = ChatEventUiElm.MoveMessageClicked(1)
                    val expectedMappedEvent = ChatEventElm.Ui.MoveMessageClicked(1)

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is MessageMovedToNewTopic") {
                    val uiEvent = ChatEventUiElm.MessageMovedToNewTopic("topic")
                    val expectedMappedEvent = ChatEventElm.Ui.MessageMovedToNewTopic("topic")

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }

                And("Ui event is ReloadMessageClicked") {
                    val uiEvent = ChatEventUiElm.ReloadMessageClicked(1)
                    val expectedMappedEvent = ChatEventElm.Ui.ReloadMessageClicked(1)

                    val mappedEvent = mapper.mapUiEvent(uiEvent)
                    Then("mapped event should be $expectedMappedEvent") {
                        mappedEvent shouldBe expectedMappedEvent
                    }
                }
            }
        }
    }
})