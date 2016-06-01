import ratpack.groovy.handling.GroovyChainAction

class TodoChain extends GroovyChainAction {
  @Override
  void execute() throws Exception {
    path(TodoBaseHandler)
    path(':id', TodoHandler)
  }
}
