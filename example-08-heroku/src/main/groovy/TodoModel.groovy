import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic

@CompileStatic
class TodoModel {
  final Long id
  final String title
  final boolean completed
  final Integer order
  private final String baseUrl

  @JsonCreator
  TodoModel(@JsonProperty("id") Long id,
              @JsonProperty("title") String title,
              @JsonProperty("completed") boolean completed,
              @JsonProperty("order") Integer order) {
    this(id, title, completed, order, null)
  }

  TodoModel(Long id, String title, boolean completed, Integer order, String baseUrl) {
    this.id = id
    this.title = title
    this.completed = completed
    this.order = order
    this.baseUrl = baseUrl
  }

  TodoModel baseUrl(String baseUrl) {
    return new TodoModel(id, title, completed, order, baseUrl)
  }

  String getUrl() {
    return "$baseUrl/$id"
  }

}

