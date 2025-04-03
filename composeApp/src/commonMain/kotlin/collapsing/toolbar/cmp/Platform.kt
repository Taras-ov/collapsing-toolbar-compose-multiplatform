package collapsing.toolbar.cmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform