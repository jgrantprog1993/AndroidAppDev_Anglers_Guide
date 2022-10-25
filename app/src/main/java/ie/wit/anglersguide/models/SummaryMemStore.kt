package ie.wit.anglersguide.models

import timber.log.Timber


var lastId2 = 0L

internal fun getId2(): Long {
    return lastId2++
}

class SummaryMemStore : SummaryStore {

    val summaries = ArrayList<SummaryModel>()

    override fun findAll(): List<SummaryModel> {
        return summaries
    }

    override fun findById(id:Long) : SummaryModel? {
        val foundSummary: SummaryModel? = summaries.find { it.id == id }
        return foundSummary
    }

    override fun create(summary: SummaryModel) {
        summary.id = getId2()
        summaries.add(summary)
        logAll()
    }

    fun logAll() {
        Timber.v("** Donations List **")
        summaries.forEach { Timber.v("Donate ${it}") }
    }
}