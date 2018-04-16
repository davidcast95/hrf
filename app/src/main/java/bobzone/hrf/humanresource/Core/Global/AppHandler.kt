package bobzone.hrf.humanresource.Core.Global

import java.util.logging.Handler

/**
 * Created by davidwibisono on 12/23/17.
 */

class AppHandler private constructor() {
    private object Holder { val INSTANCE = AppHandler() }

    companion object {
        val instance: AppHandler by lazy { Holder.INSTANCE }
    }
    class HandlerMap  {
        public var name = ""
        public var handler: Handler? = null
        constructor(handler:Handler, name:String="") {
            this.handler = handler
            this.name = name
        }

    }

    private var handling:MutableList<HandlerMap> = mutableListOf()

    fun addNewHandler(handle:Handler, name:String) {
        var handlerMap = HandlerMap(handle,name)
        handling.add(handlerMap)
    }
}