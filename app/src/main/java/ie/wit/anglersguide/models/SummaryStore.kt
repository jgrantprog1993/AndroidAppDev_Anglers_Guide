package ie.wit.anglersguide.models

interface SummaryStore {
    fun findAll() : List<SummaryModel>
    fun findById(id: Long) : SummaryModel?
    fun create(summary: SummaryModel)
}