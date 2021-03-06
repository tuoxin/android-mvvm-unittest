package cchcc.learn.amu.e07

import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.appcompat.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.TransitionSet
import android.view.MenuItem
import cchcc.learn.amu.R
import cchcc.learn.amu.databinding.ActivityE07WordBinding
import cchcc.learn.amu.e07.coordinator.E07NavigatorImpl
import cchcc.learn.amu.util.ChangeTextSize
import kotlinx.android.synthetic.main.activity_e07_word.*

class E07WordActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders
                .of(this, E07ViewModelFactory())
                .get(E07WordViewModel::class.java).apply {
                    coordinator.navigator = navigator
                }
    }

    private val navigator by lazy { E07NavigatorImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityE07WordBinding>(this, R.layout.activity_e07_word).let {
            it.setLifecycleOwner(this)
            it.viewModel = viewModel
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val transitionName = intent.getStringExtra("transitionName")
        ViewCompat.setTransitionName(tv_word, transitionName)

        val text = intent.getStringExtra("text")
        viewModel.word.value = text


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val startSize = intent.getFloatExtra("startSize", 0.0f)
            val endSize = tv_word.textSize

            setEnterSharedElementCallback(E07WordSharedElementCallback(startSize, endSize))

            window.sharedElementEnterTransition = TransitionSet().apply {
                addTransition(ChangeBounds())
                addTransition(ChangeTextSize())
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            viewModel.backToWordList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
