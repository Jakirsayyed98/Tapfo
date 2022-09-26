package app.tapho.ui.games

import android.os.Bundle
import androidx.activity.viewModels
import app.tapho.databinding.ActivityGamesHomeBinding
import app.tapho.ui.BaseActivity
import app.tapho.viewmodels.GamesViewModel

class GamesHomeActivity : BaseActivity<ActivityGamesHomeBinding>() {
   private val gamesViewModel: GamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun init(){

    }
}