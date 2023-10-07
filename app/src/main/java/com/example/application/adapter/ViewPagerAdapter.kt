import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * author: Jasmine
 * 创建时间: 2021/10/14
 * Describe:
 */
class ViewPagerAdapter(fm : FragmentManager, private val list : List<Fragment>, private val titles:List<String>) : FragmentPagerAdapter(fm){

    override fun getCount(): Int {
        return  list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}