package cl.duocuc.gymcel.data.api.exercise

enum class ExerciseDbSortParameter(val value: String){
    NAME("name"),
    EXERCISE_ID("exerciseId"),
    TARGET_MUSCLE("targetMuscle"),
    BODY_PART("bodyParts"),
    EQUIPMENTS("equipments");

    enum class Order(val value: String){
        ASCENDING("asc"),
        DESCENDING("desc")
    }

}