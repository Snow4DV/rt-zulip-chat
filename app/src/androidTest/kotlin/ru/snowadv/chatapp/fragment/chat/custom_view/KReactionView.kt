package ru.snowadv.chatapp.fragment.chat.custom_view

import android.view.View
import android.widget.TextView
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedDiagnosingMatcher
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import ru.snowadv.chat_presentation.chat.ui.view.ReactionView

class KReactionView : KBaseView<KReactionView>, ReactionViewAssertions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function)
    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function)
}

interface ReactionViewAssertions : BaseAssertions {
    /**
     * Checks if user added such reaction
     */
    fun isUserReacted() = isSelected()
    /**
     * Checks if reaction has given count
     */
    fun hasCount(count: Int) {
        view.check(
            ViewAssertions.matches(
                WithReactionCountMatcher(`is`(count))
            )
        )
    }
    /**
     * Checks if reaction has given emoji code
     */
    fun hasEmojiCode(code: String) {
        view.check(
            ViewAssertions.matches(
                WithReactionEmojiCodeMatcher(`is`(code))
            )
        )
    }

    /**
     * Checks if ReactionView adds reactions
     */
    fun isPlus(isPlus: Boolean) {
        view.check(
            ViewAssertions.matches(
                WithReactionIsAddButtonMatcher(`is`(isPlus))
            )
        )
    }
}


class WithReactionCountMatcher(private val countMatcher: Matcher<Int>) :
    TypeSafeMatcher<View>(View::class.java) {

    override fun describeTo(desc: Description) {
        desc.appendText("reactionView.count to match: ")
        countMatcher.describeTo(desc)
    }

    override fun matchesSafely(view: View?): Boolean {
        if (view !is ReactionView) {
            return false
        }
        return countMatcher.matches(view.count)
    }
}

class WithReactionEmojiCodeMatcher(private val codeMatcher: Matcher<String>) :
    TypeSafeMatcher<View>(View::class.java) {

    override fun describeTo(desc: Description) {
        desc.appendText("reactionView.emojiCode to match: ")
        codeMatcher.describeTo(desc)
    }

    override fun matchesSafely(view: View?): Boolean {
        if (view !is ReactionView) {
            return false
        }
        return codeMatcher.matches(view.emojiCode)
    }
}

class WithReactionIsAddButtonMatcher(private val isPlusMatcher: Matcher<Boolean>) :
    TypeSafeMatcher<View>(View::class.java) {

    override fun describeTo(desc: Description) {
        desc.appendText("reactionView.isPlus to match: ")
        isPlusMatcher.describeTo(desc)
    }

    override fun matchesSafely(view: View?): Boolean {
        if (view !is ReactionView) {
            return false
        }
        return isPlusMatcher.matches(view.isPlus)
    }
}
