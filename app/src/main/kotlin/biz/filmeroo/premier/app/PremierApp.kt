package biz.filmeroo.premier.app

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class PremierApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}