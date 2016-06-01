import ratpack.groovy.handling.GroovyChainAction

class TodoChain2 extends GroovyChainAction {
  @Override
  void execute() throws Exception {
    path(TodoBaseHandler2)
    path(':id', TodoHandler) // <1>
  }
}
