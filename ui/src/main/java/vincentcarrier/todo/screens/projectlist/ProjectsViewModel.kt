package vincentcarrier.todo.screens.projectlist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import vincentcarrier.todo.data.ProjectsRepo
import vincentcarrier.todo.data.models.Project


class ProjectsViewModel(app: Application, private val repo: ProjectsRepo) : AndroidViewModel(app) {

  private val controller = ProjectListController()

  internal val adapter = controller.adapter

  internal fun whenProjectsLoaded(): Observable<List<Project>> {
    return repo.whenLoaded()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { controller.setData(it) }
  }

  private inner class ProjectListController: TypedEpoxyController<List<Project>>() {
    override fun buildModels(Projects: List<Project>) {
      Projects.forEach { project ->
        projectItemView {
          id(project.id)
          name(project.name)
          projectId(project.id)
        }
      }
    }
  }
}

class ProjectsVmFactory(private val app: Application, private val repo: ProjectsRepo) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST") return ProjectsViewModel(app, repo) as T
  }
}