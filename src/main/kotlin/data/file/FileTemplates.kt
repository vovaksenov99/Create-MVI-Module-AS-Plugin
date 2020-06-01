package data.file

data class FileTemplate(val name: String, val template: String, val defaultPath: String)

val templates = listOf(
        FileTemplate(
                name = ".gitignore",
                template = """
                /build
                """,
                defaultPath = ""
        ),
        FileTemplate(
                name = "AndroidManifest.xml",
                template = """
                <manifest package="%packageName%.%featureModuleName%">
                </manifest>
                """,
                defaultPath = "src/main"
        ),
        FileTemplate(
                name = "build.gradle.kts",
                template = """
                plugins {
                    id(Plugins.ANDROID_LIB_PLUGIN_WITH_DEFAULT_CONFIG)
                }
                
                dependencies {
                }
                """,
                defaultPath = ""
        ),
        FileTemplate(
                name = "fragment_%screenNameSnakeCase%.xml",
                template = """
                <?xml version="1.0" encoding="utf-8"?>
                <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
                    xmlns:app="http://schemas.android.com/apk/res-auto"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent">
                
                </androidx.constraintlayout.widget.ConstraintLayout>
                """,
                defaultPath = "src/main/res/layout"
        ),
        FileTemplate(
                name = "%screenName%Component.kt",
                template = """
                package %packageName%.%featureModuleName%.di

                import dagger.Component
                import %packageName%.%featureModuleName%.%screenName%Deps
                import %packageName%.%featureModuleName%.presentation.%screenName%Fragment
                
                @Component(modules = [ViewModelModule::class, SomeModule::class], dependencies = [%screenName%Deps::class])
                interface %screenName%Component {
                
                    fun inject(fragment: %screenName%Fragment)
                
                    @Component.Factory
                    interface Factory {
                        fun create(deps: %screenName%Deps): %screenName%Component
                    }
                
                }
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/di"
        ),
        FileTemplate(
                name = "%screenName%Coordinator.kt",
                template = """
                package %packageName%.%featureModuleName%.navigation
                
                interface %screenName%Coordinator {
                    fun open(data: String)
                }
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/navigation"
        ),
        FileTemplate(
                name = "%screenName%Deps.kt",
                template = """
                package %packageName%.%featureModuleName%
                
                import %packageName%.%featureModuleName%.navigation.%screenName%Coordinator
                
                interface %screenName%Deps {
                    fun %screenNameLowerCase%Coordinator(): %screenName%Coordinator
                }
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%"
        ),
        FileTemplate(
                name = "%screenName%Fragment.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import android.os.Bundle
                import android.view.LayoutInflater
                import android.view.ViewGroup
                import me.vponomarenko.injectionmanager.IHasComponent
                import me.vponomarenko.injectionmanager.x.XInjectionManager
                import ru.touchin.mvi_arch.core.MviFragment
                import %packageName%.%featureModuleName%.databinding.Fragment%screenName%Binding
                import %packageName%.%featureModuleName%.di.Dagger%screenName%Component
                import %packageName%.%featureModuleName%.di.%screenName%Component
                
                class %screenName%Fragment(navArgs: %screenName%NavArgs) :
                        MviFragment<Fragment%screenName%Binding, %screenName%NavArgs, %screenName%ViewState, %screenName%ViewAction, %screenName%ViewModel>(navArgs),
                        IHasComponent<%screenName%Component> {
                
                    override val viewModel: %screenName%ViewModel by viewModel()
                
                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                
                        injectDependencies()
                    }
                
                    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): Fragment%screenName%Binding =
                            Fragment%screenName%Binding.inflate(inflater, container, false)
                
                    override fun onViewCreated(binding: Fragment%screenName%Binding, savedInstanceState: Bundle?) {
                
                    }
                
                    override fun getComponent(): %screenName%Component = Dagger%screenName%Component
                            .factory()
                            .create(XInjectionManager.findComponent())
                
                    override fun renderState(viewState: %screenName%ViewState) {
                
                    }
                
                    private fun injectDependencies() {
                        XInjectionManager.bindComponent(this)
                                .inject(this)
                    }
                
                }
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%NavArgs.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import android.os.Parcelable
                import kotlinx.android.parcel.Parcelize
                
                @Parcelize
                data class %screenName%NavArgs(): Parcelable
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%Repository.kt",
                template = """
                package %packageName%.%featureModuleName%.data
                
                import javax.inject.Inject
                
                class %screenName%Repository @Inject constructor() {}
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/data"
        ),
        FileTemplate(
                name = "%screenName%UseCase.kt",
                template = """
                package %packageName%.%featureModuleName%.domain
                
                import %packageName%.%featureModuleName%.data.%screenName%Repository
                import javax.inject.Inject
                
                class %screenName%UseCase @Inject constructor(
                        private val repository: %screenName%Repository
                )
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/domain"
        ),
        FileTemplate(
                name = "%screenName%ViewAction.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import ru.touchin.mvi_arch.core.ViewAction
                
                sealed class %screenName%ViewAction : ViewAction
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%ViewModel.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import android.os.Parcelable
                import androidx.lifecycle.SavedStateHandle
                import androidx.lifecycle.viewModelScope
                import com.squareup.inject.assisted.Assisted
                import com.squareup.inject.assisted.AssistedInject
                import kotlinx.coroutines.launch
                import ru.touchin.mvi_arch.core.MviImplViewModel
                import ru.touchin.mvi_arch.di.ViewModelAssistedFactory
                import %packageName%.%featureModuleName%.domain.Get%screenName%UseCase
                import %packageName%.%featureModuleName%.navigation.%screenName%Coordinator
                
                class %screenName%ViewModel @AssistedInject constructor(
                        @Assisted arg0: SavedStateHandle,
                        private val coordinator: %screenName%Coordinator,
                        private val useCase: %screenName%UseCase
                ) : MviImplViewModel<%screenName%NavArgs, %screenName%ViewAction, SideEffect, %screenName%ViewState>(
                        initialState = %screenName%ViewState.Default,
                        handle = arg0
                ) {
                
                    init {
                
                    }
                
                    @AssistedInject.Factory
                    interface Factory : ViewModelAssistedFactory<%screenName%ViewModel>
                
                    sealed class SideEffect : ru.touchin.mvi_arch.core.SideEffect
                
                }
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%ViewState.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import ru.touchin.mvi_arch.core.ViewState
                
                sealed class %screenName%ViewState : ViewState {
                    object Default: %screenName%ViewState
                }
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "SomeModule.kt",
                template = """
                package %packageName%.%featureModuleName%.di
                
                import dagger.Module
                
                @Module
                abstract class SomeModule
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/di"
        ),
        FileTemplate(
                name = "ViewModelModule.kt",
                template = """
                package %packageName%.%featureModuleName%.di
                
                import androidx.lifecycle.ViewModel
                import com.squareup.inject.assisted.dagger2.AssistedModule
                import dagger.Binds
                import dagger.Module
                import dagger.multibindings.IntoMap
                import ru.touchin.mvi_arch.di.ViewModelAssistedFactory
                import ru.touchin.mvi_arch.di.ViewModelKey
                import %packageName%.%featureModuleName%.presentation.%screenName%ViewModel
                
                @AssistedModule
                @Module(includes = [AssistedInject_ViewModelAssistedFactoriesModule::class])
                interface ViewModelModule {
                
                    @Binds
                    @IntoMap
                    @ViewModelKey(%screenName%ViewModel::class)
                    fun bind%screenName%VMFactory(factory: %screenName%ViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
                
                }
                """,
                defaultPath = "src/main/%packageName%/%featureModuleName%/di"
        )


)