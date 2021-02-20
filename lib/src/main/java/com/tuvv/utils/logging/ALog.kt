package com.tuvv.utils.logging

@Suppress("unused")
class ALog {

    companion object {
        const val VERBOSE   = 2
        const val DEBUG     = 3
        const val INFO      = 4
        const val WARN      = 5
        const val ERROR     = 6

        @JvmStatic fun e(msg: String) { log(ERROR, msg) }
        @JvmStatic fun w(msg: String) { log(WARN, msg) }
        @JvmStatic fun i(msg: String) { log(INFO, msg) }
        @JvmStatic fun d(msg: String) { log(DEBUG, msg) }
        @JvmStatic fun v(msg: String) { log(VERBOSE, msg) }

        @JvmStatic fun e(tag: String, msg: String) { log(ERROR, tag, msg) }
        @JvmStatic fun w(tag: String, msg: String) { log(WARN, tag, msg) }
        @JvmStatic fun i(tag: String, msg: String) { log(INFO, tag, msg) }
        @JvmStatic fun d(tag: String, msg: String) { log(DEBUG, tag, msg) }
        @JvmStatic fun v(tag: String, msg: String) { log(VERBOSE, tag, msg) }

        @JvmStatic fun log(priority: String, tag: String, msg: String) {
            when (priority) {
                "e" -> log(ERROR, tag, msg)
                "w" -> log(WARN, tag, msg)
                "i" -> log(INFO, tag, msg)
                "d" -> log(DEBUG, tag, msg)
                "v" -> log(VERBOSE, tag, msg)
            }
        }

        @Suppress("unused")
        @JvmStatic fun setLevel(level: Int) {
            ALog.level = level
        }

        @JvmStatic fun setTag(tag: String?) {
            appTag = if (!tag.isNullOrEmpty()) "$tag " else ""
        }

        @JvmStatic fun addOutput(output: IMessageOutput) {
            outputs[output.javaClass] = output
        }

        @JvmStatic fun clearOutput() {
            outputs.clear()
        }

        private var level = INFO
        private var appTag = ""
        private var classAsTag = true
        private val outputs: MutableMap<Class<IMessageOutput>, IMessageOutput> = HashMap()

        private fun log(priority: Int, msg: String) {
            val invokingClassName = if (classAsTag) getInvokingClassName() else ""
            log(priority, invokingClassName, msg)
        }

        private fun log(priority: Int, tag: String, msg: String) {
            if (priority >= level) {
                outputs.forEach { x -> x.value.send(priority, "$appTag$tag", msg) }
            }
        }


        //Helpers
        private fun getInvokingClassName(): String {
            val trace =
                Thread.currentThread().stackTrace

            val stackIndex = getStackOffset(trace)
            return  getSimpleClassName(trace[stackIndex].className)
        }

        /**
         * Determines the starting index of the stack trace, after method calls made by this class.
         *
         * @param trace the stack trace
         * @return the stack offset
         */
        private fun getStackOffset(trace: Array<StackTraceElement>): Int {
            var i = MIN_STACK_OFFSET
            while (i < trace.size) {
                val name = trace[i].className
                if (!name.contains(ALog::class.java.name)) {
                    return i
                }
                i++
            }
            return -1
        }

        private fun getSimpleClassName(name: String): String {
            val lastIndex = name.lastIndexOf(".")
            return name.substring(lastIndex + 1)
        }


        private const val MIN_STACK_OFFSET = 4
    }

}