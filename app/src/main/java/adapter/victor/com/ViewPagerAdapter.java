package adapter.victor.com;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentPagerAdapter;import java.util.List;/** * Created by victor on 2016/1/21. */public class ViewPagerAdapter extends FragmentPagerAdapter {    private List<String> titles;    private List<Fragment> frags;    public void setTitles(List<String> titles) {        this.titles = titles;    }    public void setFrags(List<Fragment> frags) {        this.frags = frags;    }    public List<String> getTitles() {        return titles;    }    public List<Fragment> getFrags() {        return frags;    }    public ViewPagerAdapter(FragmentManager fm) {        super(fm);        this.titles = titles;        this.frags = frags;    }    @Override    public CharSequence getPageTitle(int position) {        return titles.get(position);    }    @Override    public Fragment getItem(int position) {        return frags.get(position);    }    @Override    public int getCount() {        return titles.size();    }}