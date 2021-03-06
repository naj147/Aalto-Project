package com.jeomix.android.gpstracker.files.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.EventBusClasses.LocationEvents;
import com.jeomix.android.gpstracker.files.NonSwipeableViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by jeomix on 8/12/17.
 */

    public class FragmentPagerSupport extends AppCompatActivity {
        static final int NUM_ITEMS = 3;
        RecyclerView recyclerView;
//        Fragment_results fr;
//        Fragment_history fh;
        MyAdapter mAdapter;
        TabLayout tabLayout;
        NonSwipeableViewPager mPager;
        public NavigationTabBar navigationTabBar=null;

//        @Subscribe(threadMode = ThreadMode.MAIN)
//        public void onResult_Array(Result_Array event) {
//            Toast.makeText(getApplicationContext(), event.getTextProfilName(), Toast.LENGTH_SHORT).show();
//
//            mPager.postDelayed(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    mPager.setCurrentItem(1, true);
//                }
//            }, 300);
//
//        }
//        @Subscribe(threadMode = ThreadMode.MAIN)
//        public void onHistory_Array(History_Array event) {
//            Toast.makeText(getApplicationContext(), event.getResult().getTextProfilName(), Toast.LENGTH_SHORT).show();
//            mPager.postDelayed(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    mPager.setCurrentItem(1, true);
//                }
//            }, 300);
//
//        }
@Subscribe(threadMode = ThreadMode.MAIN)
public void onLocationEvents(LocationEvents locationEvents){
   mPager.setCurrentItem(0,true);
}
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBoolean(Boolean event){
        if(event)
        mPager.setCurrentItem(1,true);
    }
        @Override
        protected void onStop() {
            EventBus.getDefault().unregister(this);
            super.onStop();
        }

        @Override
        protected void onStart() {
            EventBus.getDefault().register(this);
            super.onStart();

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.fragment_pager);

            mAdapter = new MyAdapter(getSupportFragmentManager(),FragmentPagerSupport.this);

            mPager = (NonSwipeableViewPager)findViewById(R.id.pager);
            mPager.setAdapter(mAdapter);
            mPager.setOffscreenPageLimit(3);
        /*    mpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (position == 0 && positionOffset > 0.5) {
                mpager.setCurrentItem(MANDATORY_PAGE_LOCATION, true);
            }
//        }*/
//            if(mAdapter.fr!=null){
//                fr=mAdapter.fr;
//            }
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mPager.getAdapter().notifyDataSetChanged();

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb);
            final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(this,R.drawable.nav_marker),
                            Color.parseColor("#df5a55")
                    ).title("Map")
                            .badgeTitle("20")
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(this,R.drawable.nav_car),
                            Color.parseColor("#76afcf")
                    ).title("Vehicles")
                            .badgeTitle("8")
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(this,R.drawable.nav_user),
                            Color.parseColor("#72d3b4")
                    ).title("Users")
                            .badgeTitle("NTB")
                            .build()
            );
            navigationTabBar.setModels(models);
            navigationTabBar.setViewPager(mPager,1);
            navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(final int position) {
                    navigationTabBar.getModels().get(position).hideBadge();
                    mPager.setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(final int state) {

                }
            });


//            navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
//            navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
//            navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
//            navigationTabBar.setIsBadged(true);
//            navigationTabBar.setIsTitled(true);
//            navigationTabBar.setIsTinted(true);
//            navigationTabBar.setIsBadgeUseTypeface(true);
//            navigationTabBar.setBadgeBgColor(Color.RED);
//            navigationTabBar.setBadgeTitleColor(Color.WHITE);
//            navigationTabBar.setIsSwiped(true);
//            navigationTabBar.setBgColor(Color.BLACK);
//            navigationTabBar.setBadgeSize(10);
//            navigationTabBar.setTitleSize(10);
//            navigationTabBar.setIconSizeFraction(1/2);
/**navigationTabBar.setModels(models);
 navigationTabBar.setViewPager(viewPager, 2);

 navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
 navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
 navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
 navigationTabBar.setTypeface("fonts/custom_font.ttf");
 navigationTabBar.setIsBadged(true);
 navigationTabBar.setIsTitled(true);
 navigationTabBar.setIsTinted(true);
 navigationTabBar.setIsBadgeUseTypeface(true);
 navigationTabBar.setBadgeBgColor(Color.RED);
 navigationTabBar.setBadgeTitleColor(Color.WHITE);
 navigationTabBar.setIsSwiped(true);
 navigationTabBar.setBgColor(Color.BLACK);
 navigationTabBar.setBadgeSize(10);
 navigationTabBar.setTitleSize(10);
 navigationTabBar.setIconSizeFraction(0.5);
 * */

            // Watch for button clicks.
//        Button button = (Button)findViewById(R.id.goto_first);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(0);
//            }
//        });
//        button = (Button)findViewById(R.id.goto_last);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(NUM_ITEMS-1);
//            }
//        });
//            tabLayout = (TabLayout)findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
//            tabLayout.setupWithViewPager(mPager);
     /*   tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mPager);
            }
        });*/
        }

        public static class MyAdapter extends FragmentPagerAdapter {
            private FragmentManager fragmentManager;
            private Context context;
//            private Fragment_results fr;
//            private Fragment_history fh;
            public MyAdapter(FragmentManager fm) {
                super(fm);
            }
            public MyAdapter(FragmentManager fm, Context context) {
                super(fm);
                this.context=context;
                this.fragmentManager=fm;

            }

            @Override
            public int getCount() {
                return NUM_ITEMS;
            }

            // this needs to be overided to load more than one fragmenet with the case thing
            @Override
            public Fragment getItem(int position) {
                switch (position) {

                    case 0:
                        return new MapsActivity();
                    case 1:
                        Fragment_Users fr = new Fragment_Users();
                        fr.switch_loading();
                        return fr;
                    case 2:
//                        fh=new Fragment_history();
//                        return fh;
                    default:
                        return new Fragment_Users();
//                        return new Fragment_swipes();
                }
            }
            //Overried this too
//            @Override
//            public CharSequence getPageTitle(int position) {
//                String[] pageTitles =context.getResources().getStringArray(R.array.page_titles_array);
//                return pageTitles[position];
//            }
//        @Override
//        public int getItemPosition(Object object) {
//// POSITION_NONE makes it possible to reload the PagerAdapter
//            if (object instanceof Fragment_swipes) {
//                return POSITION_NONE;
//            } else
//                return POSITION_UNCHANGED;
//        }
        }

//        public static class ArrayListFragment extends ListFragment {
//            int mNum;
//            final String[] Cheese={"Ugh","ogh","yay"};
//            /**
//             * Create a new instance of CountingFragment, providing "num"
//             * as an argument.
//             */
//            static ArrayListFragment newInstance(int num) {
//                ArrayListFragment f = new ArrayListFragment();
//                // Supply num input as an argument.
//                Bundle args = new Bundle();
//                args.putInt("num", num);
//                f.setArguments(args);
//                return f;
//            }
//
//            /**
//             * When creating, retrieve this instance's number from its arguments.
//             */
//            @Override
//            public void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                mNum = getArguments() != null ? getArguments().getInt("num") : 1;
//            }
//
//            /**
//             * The Fragment's UI is just a simple text view showing its
//             * instance number.
//             */
//            @Override
//            public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                     Bundle savedInstanceState) {
//                View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
//                View tv = v.findViewById(R.id.text);
//                ((TextView)tv).setText("Fragment #" + mNum);
//                return v;
//            }
//
//            @Override
//            public void onActivityCreated(Bundle savedInstanceState) {
//                super.onActivityCreated(savedInstanceState);
//                setListAdapter(new ArrayAdapter<String>(getActivity(),
//                        android.R.layout.simple_list_item_1, Cheese));
//            }
//
//            @Override
//            public void onListItemClick(ListView l, View v, int position, long id) {
//
//                Log.i("FragmentList", "Item clicked: " + id);
//            }
//
//        }

    }

