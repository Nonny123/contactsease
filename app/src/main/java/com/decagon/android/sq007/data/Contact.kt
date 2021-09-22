package com.decagon.android.sq007.data

import com.google.firebase.database.Exclude

data class Contact(
    //@get: Exclude    // don't save in database column
    var id: String? = null,
    var fullName: String? = null,
    var contactNumber: String? = null,

    @get: Exclude
    var isDeleted: Boolean = false

) {

    //regionto make sure the delete contact id is the actual id
    override fun equals(other: Any?): Boolean {
        return if (other is Contact) {
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (fullName?.hashCode() ?: 0)
        result = 31 * result + (contactNumber?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        return result
    }
    //endregion
}
