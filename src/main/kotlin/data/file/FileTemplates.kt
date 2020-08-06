package data.file

data class FileTemplate(val name: String, val template: String, val defaultPath: String)

val featureTemplates = listOf(
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
                    dagger()
                    mvi()
                    constraintLayout()
                    implementationModule(Module.Core.UI)
                    coreStrings()
                    coreNetwork()
                    sharedPrefs()
                    navigation()
                    materialDesign()
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

                import android.content.Context
                import %packageName%.%featureModuleName%.%screenName%Deps
                import %packageName%.%featureModuleName%.flow.%screenName%FlowFragment
                import %packageName%.%featureModuleName%.presentation.%screenName%Fragment
                import dagger.BindsInstance
                import dagger.Component
                import ru.touchin.roboswag.navigation_base.scopes.FeatureScope
                import ru.touchin.roboswag.navigation_cicerone.flow.FlowNavigationModule
                
                @FeatureScope
                @Component(modules = [ViewModelModule::class, FlowNavigationModule::class, CoordinatorsModule::class, SomeModule::class], dependencies = [%screenName%Deps::class])
                interface %screenName%Component {
                
                    fun inject(fragment: %screenName%Fragment)
                
                    fun inject(fragment: %screenName%FlowFragment)
                
                    @Component.Factory
                    interface Factory {
                        fun create(@BindsInstance context: Context, deps: %screenName%Deps): %screenName%Component
                    }
                
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/di"
        ),
        FileTemplate(
                name = "%screenName%CoordinatorImpl.kt",
                template = """
                package %packageName%.%featureModuleName%.navigation
                
                import %packageName%.%featureModuleName%.flow.%screenName%FlowCoordinator
                import %packageName%.%featureModuleName%.presentation.%screenName%Coordinator
                import javax.inject.Inject
                
                class %screenName%CoordinatorImpl @Inject constructor(
                        private val %screenNameLowerCase%FlowCoordinator: %screenName%FlowCoordinator
                ): %screenName%Coordinator {
                
                    override fun exit() {
                        %screenNameLowerCase%FlowCoordinator.exit()
                    }
                
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/navigation"
        ),
        FileTemplate(
                name = "Screens.kt",
                template = """
                package %packageName%.%featureModuleName%.navigation
                
                import androidx.fragment.app.Fragment
                import %packageName%.%featureModuleName%.presentation.%screenName%Fragment
                import ru.terrakok.cicerone.android.support.SupportAppScreen
                
                object Screens {
                
                    class %screenName% : SupportAppScreen() {
                        override fun getFragment(): Fragment = %screenName%Fragment()
                    }
                
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/navigation"
        ),
        FileTemplate(
                name = "%screenName%FlowCoordinator.kt",
                template = """
                package %packageName%.%featureModuleName%.flow
                
                interface %screenName%FlowCoordinator {
                    fun exit()
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/flow"
        ),
        FileTemplate(
                name = "%screenName%FlowFragment.kt",
                template = """
                package %packageName%.%featureModuleName%.flow
                        
                import %packageName%.%featureModuleName%.di.Dagger%screenName%Component
                import %packageName%.%featureModuleName%.di.%screenName%Component
                import %packageName%.%featureModuleName%.navigation.Screens
                import me.vponomarenko.injectionmanager.IHasComponent
                import me.vponomarenko.injectionmanager.x.XInjectionManager
                import ru.terrakok.cicerone.android.support.SupportAppScreen
                import ru.touchin.roboswag.navigation_cicerone.flow.FlowFragment
                
                class %screenName%FlowFragment : FlowFragment(), IHasComponent<%screenName%Component> {
                
                        override fun injectComponent() {
                                XInjectionManager.bindComponent(this).inject(this)
                        }
                        
                        override fun getLaunchScreen(): SupportAppScreen {
                                return Screens.%screenName%()
                        }
                        
                        override fun getComponent(): %screenName%Component {
                                return Dagger%screenName%Component
                                        .factory()
                                        .create(requireContext(), XInjectionManager.findComponent())
                        }
                        
                }

                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/flow"
        ),
        FileTemplate(
                name = "%screenName%Deps.kt",
                template = """
                package %packageName%.%featureModuleName%
                
                import %packageName%.%featureModuleName%.flow.%screenName%FlowCoordinator

                interface %screenName%Deps {
                    fun %screenNameLowerCase%FlowCoordinator(): %screenName%FlowCoordinator
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%"
        ),
        FileTemplate(
                name = "%screenName%Coordinator.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                interface %screenName%Coordinator {
                    fun exit()
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%Fragment.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import android.os.Bundle
                import %packageName%.%featureModuleName%.R
                import %packageName%.%featureModuleName%.databinding.Fragment%screenName%Binding
                import %packageName%.%featureModuleName%.di.%screenName%Component
                import me.vponomarenko.injectionmanager.x.XInjectionManager
                import ru.touchin.roboswag.mvi_arch.core.MviFragment
                import ru.touchin.roboswag.navigation_base.fragments.EmptyState
                import ru.touchin.roboswag.navigation_base.fragments.viewBinding
                
                class %screenName%Fragment : MviFragment<EmptyState, %screenName%ViewState, %screenName%ViewAction, %screenName%ViewModel>(R.layout.fragment_%screenNameSnakeCase%) {
                
                    private val binding by viewBinding(Fragment%screenName%Binding::bind)
                
                    override val viewModel: %screenName%ViewModel by viewModel()
                
                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        injectDependencies()
                    }
                
                    private fun injectDependencies() {
                        XInjectionManager.findComponent<%screenName%Component>()
                                .inject(this)
                    }
                
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%Repository.kt",
                template = """
                package %packageName%.%featureModuleName%.data
                
                import javax.inject.Inject
                
                class %screenName%Repository @Inject constructor() {}
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/data"
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
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/domain"
        ),
        FileTemplate(
                name = "%screenName%ViewAction.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import ru.touchin.roboswag.mvi_arch.marker.ViewAction
                
                sealed class %screenName%ViewAction : ViewAction
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%ViewModel.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import androidx.lifecycle.SavedStateHandle
                import com.squareup.inject.assisted.Assisted
                import com.squareup.inject.assisted.AssistedInject
                import %packageName%.%featureModuleName%.domain.%screenName%UseCase
                import ru.touchin.roboswag.mvi_arch.core.MviViewModel
                import ru.touchin.roboswag.mvi_arch.di.ViewModelAssistedFactory
                import ru.touchin.roboswag.navigation_base.fragments.EmptyState
                
                class %screenName%ViewModel @AssistedInject constructor(
                        @Assisted arg0: SavedStateHandle,
                        private val coordinator: %screenName%Coordinator,
                        private val %screenNameLowerCase%UseCase: %screenName%UseCase
                ) : MviViewModel<EmptyState, %screenName%ViewAction, %screenName%ViewState>(%screenName%ViewState(), arg0) {
                
                    override fun dispatchAction(action: %screenName%ViewAction) {
                        when (action) {
                            
                        }
                    }
                
                    @AssistedInject.Factory
                    interface Factory : ViewModelAssistedFactory<%screenName%ViewModel>
                
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "%screenName%ViewState.kt",
                template = """
                package %packageName%.%featureModuleName%.presentation
                
                import ru.touchin.roboswag.mvi_arch.marker.ViewState
                
                data class %screenName%ViewState(
                        val isProgress: Boolean = false 
                ) : ViewState
                
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/presentation"
        ),
        FileTemplate(
                name = "SomeModule.kt",
                template = """
                package %packageName%.%featureModuleName%.di
                
                import dagger.Module
                
                @Module
                abstract class SomeModule
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/di"
        ),
        FileTemplate(
                name = "CoordinatorsModule.kt",
                template = """
                package %packageName%.%featureModuleName%.di
                
                import %packageName%.%featureModuleName%.navigation.%screenName%CoordinatorImpl
                import %packageName%.%featureModuleName%.presentation.%screenName%Coordinator
                import dagger.Binds
                import dagger.Module
                
                @Module
                abstract class CoordinatorsModule {
                
                        @Binds
                        abstract fun %screenNameLowerCase%Coordinator(impl: %screenName%CoordinatorImpl): %screenName%Coordinator
                
                }
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/di"
        ),
        FileTemplate(
                name = "ViewModelModule.kt",
                template = """
                package %packageName%.%featureModuleName%.di
                
                import androidx.lifecycle.ViewModel
                import com.squareup.inject.assisted.dagger2.AssistedModule
                import %packageName%.%featureModuleName%.presentation.%screenName%ViewModel
                import dagger.Binds
                import dagger.Module
                import dagger.multibindings.IntoMap
                import ru.touchin.roboswag.mvi_arch.di.ViewModelAssistedFactory
                import ru.touchin.roboswag.mvi_arch.di.ViewModelKey
                
                @Module(includes = [ViewModelAssistedFactoriesModule::class])
                interface ViewModelModule {
                
                    @Binds
                    @IntoMap
                    @ViewModelKey(%screenName%ViewModel::class)
                    fun bind%screenName%VMFactory(factory: %screenName%ViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
                
                }
                
                @AssistedModule
                @Module(includes = [AssistedInject_ViewModelAssistedFactoriesModule::class])
                abstract class ViewModelAssistedFactoriesModule
                """,
                defaultPath = "src/main/java/%packageName%/%featureModuleName%/di"
        ),
        FileTemplate(
                name = "libs",
                template = "",
                defaultPath = ""
        )
)

val testTemplates = listOf(
        FileTemplate(
                name = "AndroidManifest.xml",
                template = """
                <manifest>
                """,
                defaultPath = "src/main"
        ),
        FileTemplate(
                name = "libs",
                template = "",
                defaultPath = "src/main"
        )
)