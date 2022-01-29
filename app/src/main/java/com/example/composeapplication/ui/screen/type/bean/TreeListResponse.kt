package com.example.composeapplication.ui.screen.type.bean

data class TreeListResponse(
    var errorCode: Int,
    var errorMsg: String?,
    var data: List<Knowledge>
) {
    data class Knowledge(
        var id: Int,
        var name: String,
        var courseId: Int,
        var parentChapterId: Int,
        var order: Int,
        var visible: Int,
        var children: List<Children>?
    ) {
        data class Children(
            var id: Int,
            var name: String,
            var courseId: Int,
            var parentChapterId: Int,
            var order: Int,
            var visible: Int,
            var children: List<Children>?
        )
    }
}