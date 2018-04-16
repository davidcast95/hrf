package bobzone.hrf.humanresource.Core.Base.OptionsMenu

/**
 * Created by davidwibisono on 2/13/18.
 */

open interface OptionsMenuDataSource {
    fun getOptionsMenu():Int?
    fun getSearchOptionsMenu(): SearchOptionsMenuDelegate?
}