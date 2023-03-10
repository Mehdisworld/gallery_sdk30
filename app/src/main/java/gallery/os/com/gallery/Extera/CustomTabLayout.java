package gallery.os.com.gallery.Extera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.BoolRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.tabs.TabItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import gallery.os.com.gallery.R;


@ViewPager.DecorView
public class CustomTabLayout extends HorizontalScrollView {
    @Dimension(
            unit = 0
    )
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    @Dimension(
            unit = 0
    )
    static final int DEFAULT_GAP_TEXT_ICON = 8;
    @Dimension(
            unit = 0
    )
    private static final int DEFAULT_HEIGHT = 48;
    @Dimension(
            unit = 0
    )
    private static final int TAB_MIN_WIDTH_MARGIN = 56;


    public int getTabBackgroundResId() {
        return tabBackgroundResId;
    }

    public void setTabBackgroundResId(int tabBackgroundResId) {
        this.tabBackgroundResId = tabBackgroundResId;
    }

    @Dimension(
            unit = 0
    )
    private static final int MIN_INDICATOR_WIDTH = 24;
    @Dimension(
            unit = 0
    )
    static final int FIXED_WRAP_GUTTER_MIN = 16;
    private static final int INVALID_WIDTH = -1;
    private static final int ANIMATION_DURATION = 300;
    private static final Pools.Pool<CustomTabLayout.Tab> tabPool = new Pools.SynchronizedPool(16);
    public static final int MODE_SCROLLABLE = 0;
    public static final int MODE_FIXED = 1;
    public static final int GRAVITY_FILL = 0;
    public static final int GRAVITY_CENTER = 1;
    public static final int INDICATOR_GRAVITY_BOTTOM = 0;
    public static final int INDICATOR_GRAVITY_CENTER = 1;
    public static final int INDICATOR_GRAVITY_TOP = 2;
    public static final int INDICATOR_GRAVITY_STRETCH = 3;
    private final ArrayList<CustomTabLayout.Tab> tabs;
    private CustomTabLayout.Tab selectedTab;
    private final RectF tabViewContentBounds;
    private final CustomTabLayout.SlidingTabIndicator slidingTabIndicator;
    int tabPaddingStart;
    int tabPaddingTop;
    int tabPaddingEnd;
    int tabPaddingBottom;
    int tabTextAppearance;
    ColorStateList tabTextColors;
    ColorStateList tabIconTint;
    ColorStateList tabRippleColorStateList;
    @Nullable
    Drawable tabSelectedIndicator;
    android.graphics.PorterDuff.Mode tabIconTintMode;
    float tabTextSize;
    float tabTextMultiLineSize;
     int tabBackgroundResId;
    int tabMaxWidth;
    private final int requestedTabMinWidth;
    private final int requestedTabMaxWidth;
    private final int scrollableTabMinWidth;
    private int contentInsetStart;
    int tabGravity;
    int tabIndicatorAnimationDuration;
    int tabIndicatorGravity;
    int mode;
    boolean inlineLabel;
    boolean tabIndicatorFullWidth;
    boolean unboundedRipple;
    private CustomTabLayout.BaseOnTabSelectedListener selectedListener;
    private final ArrayList<CustomTabLayout.BaseOnTabSelectedListener> selectedListeners;
    private CustomTabLayout.BaseOnTabSelectedListener currentVpSelectedListener;
    private ValueAnimator scrollAnimator;
    ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private DataSetObserver pagerAdapterObserver;
    private CustomTabLayout.TabLayoutOnPageChangeListener pageChangeListener;
    private CustomTabLayout.AdapterChangeListener adapterChangeListener;
    private boolean setupViewPagerImplicitly;
    private final Pools.Pool<CustomTabLayout.TabView> tabViewPool;

    public CustomTabLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.tabStyle);
    }

    @SuppressLint("RestrictedApi")
    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tabs = new ArrayList();
        this.tabViewContentBounds = new RectF();
        this.tabMaxWidth = 2147483647;
        this.selectedListeners = new ArrayList();
        this.tabViewPool = new Pools.SimplePool(12);
        this.setHorizontalScrollBarEnabled(false);
        this.slidingTabIndicator = new CustomTabLayout.SlidingTabIndicator(context);
        super.addView(this.slidingTabIndicator, 0, new LayoutParams(-2, -1));
        @SuppressLint("RestrictedApi") TypedArray a = ThemeEnforcement.obtainStyledAttributes(context, attrs, R.styleable.TabLayout, defStyleAttr, R.style.Widget_Design_TabLayout, new int[]{R.styleable.TabLayout_tabTextAppearance});
        this.slidingTabIndicator.setSelectedIndicatorHeight(a.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, -1));
        this.slidingTabIndicator.setSelectedIndicatorColor(a.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
        this.setSelectedTabIndicator(MaterialResources.getDrawable(context, a, R.styleable.TabLayout_tabIndicator));
        this.setSelectedTabIndicatorGravity(a.getInt(R.styleable.TabLayout_tabIndicatorGravity, 0));
        this.setTabIndicatorFullWidth(a.getBoolean(R.styleable.TabLayout_tabIndicatorFullWidth, true));
        this.tabPaddingStart = this.tabPaddingTop = this.tabPaddingEnd = this.tabPaddingBottom = a.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
        this.tabPaddingStart = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
        this.tabPaddingTop = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
        this.tabPaddingEnd = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
        this.tabPaddingBottom = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
        this.tabTextAppearance = a.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
        TypedArray ta = context.obtainStyledAttributes(this.tabTextAppearance, R.styleable.TextAppearance);

        try {
            this.tabTextSize = (float)ta.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
            this.tabTextColors = MaterialResources.getColorStateList(context, ta, R.styleable.TextAppearance_android_textColor);
        } finally {
            ta.recycle();
        }

        if(a.hasValue(R.styleable.TabLayout_tabTextColor)) {
            this.tabTextColors = MaterialResources.getColorStateList(context, a, R.styleable.TabLayout_tabTextColor);
        }

        if(a.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
            int selected = a.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
            this.tabTextColors = createColorStateList(this.tabTextColors.getDefaultColor(), selected);
        }

        this.tabIconTint = MaterialResources.getColorStateList(context, a, R.styleable.TabLayout_tabIconTint);
        this.tabIconTintMode = ViewUtils.parseTintMode(a.getInt(R.styleable.TabLayout_tabIconTintMode, -1), (android.graphics.PorterDuff.Mode)null);
        this.tabRippleColorStateList = MaterialResources.getColorStateList(context, a, R.styleable.TabLayout_tabRippleColor);
        this.tabIndicatorAnimationDuration = a.getInt(R.styleable.TabLayout_tabIndicatorAnimationDuration, 300);
        this.requestedTabMinWidth = a.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
        this.requestedTabMaxWidth = a.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
        this.tabBackgroundResId = a.getResourceId(R.styleable.TabLayout_tabBackground, 0);
        this.contentInsetStart = a.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
        this.mode = a.getInt(R.styleable.TabLayout_tabMode, 1);
        this.tabGravity = a.getInt(R.styleable.TabLayout_tabGravity, 0);
        this.inlineLabel = a.getBoolean(R.styleable.TabLayout_tabInlineLabel, false);
        this.unboundedRipple = a.getBoolean(R.styleable.TabLayout_tabUnboundedRipple, false);
        a.recycle();
        Resources res = this.getResources();
        this.tabTextMultiLineSize = (float)res.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
        this.scrollableTabMinWidth = res.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
        this.applyModeAndGravity();
    }

    public void setSelectedTabIndicatorColor(@ColorInt int color) {
        this.slidingTabIndicator.setSelectedIndicatorColor(color);
    }

    /** @deprecated */
    @Deprecated
    public void setSelectedTabIndicatorHeight(int height) {
        this.slidingTabIndicator.setSelectedIndicatorHeight(height);
    }

    public void setScrollPosition(int position, float positionOffset, boolean updateSelectedText) {
        this.setScrollPosition(position, positionOffset, updateSelectedText, true);
    }

    void setScrollPosition(int position, float positionOffset, boolean updateSelectedText, boolean updateIndicatorPosition) {
        int roundedPosition = Math.round((float)position + positionOffset);
        if(roundedPosition >= 0 && roundedPosition < this.slidingTabIndicator.getChildCount()) {
            if(updateIndicatorPosition) {
                this.slidingTabIndicator.setIndicatorPositionFromTabPosition(position, positionOffset);
            }

            if(this.scrollAnimator != null && this.scrollAnimator.isRunning()) {
                this.scrollAnimator.cancel();
            }

            this.scrollTo(this.calculateScrollXForTab(position, positionOffset), 0);
            if(updateSelectedText) {
                this.setSelectedTabView(roundedPosition);
            }

        }
    }

    public void addTab(@NonNull CustomTabLayout.Tab tab) {
        this.addTab(tab, this.tabs.isEmpty());
    }

    public void addTab(@NonNull CustomTabLayout.Tab tab, int position) {
        this.addTab(tab, position, this.tabs.isEmpty());
    }

    public void addTab(@NonNull CustomTabLayout.Tab tab, boolean setSelected) {
        this.addTab(tab, this.tabs.size(), setSelected);
    }

    public void addTab(@NonNull CustomTabLayout.Tab tab, int position, boolean setSelected) {
        if(tab.parent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        } else {
            this.configureTab(tab, position);
            this.addTabView(tab);
            if(setSelected) {
                tab.select();
            }

        }
    }

    private void addTabFromItemView(@NonNull TabItem item) {
        CustomTabLayout.Tab tab = this.newTab();
        if(item.text != null) {
            tab.setText(item.text);
        }

        if(item.icon != null) {
            tab.setIcon(item.icon);
        }

        if(item.customLayout != 0) {
            tab.setCustomView(item.customLayout);
        }

        if(!TextUtils.isEmpty(item.getContentDescription())) {
            tab.setContentDescription(item.getContentDescription());
        }

        this.addTab(tab);
    }

    /** @deprecated */
    @Deprecated
    public void setOnTabSelectedListener(@Nullable CustomTabLayout.BaseOnTabSelectedListener listener) {
        if(this.selectedListener != null) {
            this.removeOnTabSelectedListener(this.selectedListener);
        }

        this.selectedListener = listener;
        if(listener != null) {
            this.addOnTabSelectedListener(listener);
        }

    }

    public void addOnTabSelectedListener(@NonNull CustomTabLayout.BaseOnTabSelectedListener listener) {
        if(!this.selectedListeners.contains(listener)) {
            this.selectedListeners.add(listener);
        }

    }

    public void removeOnTabSelectedListener(@NonNull CustomTabLayout.BaseOnTabSelectedListener listener) {
        this.selectedListeners.remove(listener);
    }

    public void clearOnTabSelectedListeners() {
        this.selectedListeners.clear();
    }

    @NonNull
    public CustomTabLayout.Tab newTab() {
        CustomTabLayout.Tab tab = this.createTabFromPool();
        tab.parent = this;
        tab.view = this.createTabView(tab);
        return tab;
    }

    protected CustomTabLayout.Tab createTabFromPool() {
        CustomTabLayout.Tab tab = (CustomTabLayout.Tab)tabPool.acquire();
        if(tab == null) {
            tab = new CustomTabLayout.Tab();
        }

        return tab;
    }

    protected boolean releaseFromTabPool(CustomTabLayout.Tab tab) {
        return tabPool.release(tab);
    }

    public int getTabCount() {
        return this.tabs.size();
    }

    @Nullable
    public CustomTabLayout.Tab getTabAt(int index) {
        return index >= 0 && index < this.getTabCount()?(CustomTabLayout.Tab)this.tabs.get(index):null;
    }

    public int getSelectedTabPosition() {
        return this.selectedTab != null?this.selectedTab.getPosition():-1;
    }

    public void removeTab(CustomTabLayout.Tab tab) {
        if(tab.parent != this) {
            throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
        } else {
            this.removeTabAt(tab.getPosition());
        }
    }

    public void removeTabAt(int position) {
        int selectedTabPosition = this.selectedTab != null?this.selectedTab.getPosition():0;
        this.removeTabViewAt(position);
        CustomTabLayout.Tab removedTab = (CustomTabLayout.Tab)this.tabs.remove(position);
        if(removedTab != null) {
            removedTab.reset();
            this.releaseFromTabPool(removedTab);
        }

        int newTabCount = this.tabs.size();

        for(int i = position; i < newTabCount; ++i) {
            ((CustomTabLayout.Tab)this.tabs.get(i)).setPosition(i);
        }

        if(selectedTabPosition == position) {
            this.selectTab(this.tabs.isEmpty()?null:(CustomTabLayout.Tab)this.tabs.get(Math.max(0, position - 1)));
        }

    }

    public void removeAllTabs() {
        for(int i = this.slidingTabIndicator.getChildCount() - 1; i >= 0; --i) {
            this.removeTabViewAt(i);
        }

        Iterator i = this.tabs.iterator();

        while(i.hasNext()) {
            CustomTabLayout.Tab tab = (CustomTabLayout.Tab)i.next();
            i.remove();
            tab.reset();
            this.releaseFromTabPool(tab);
        }

        this.selectedTab = null;
    }

    public void setTabMode(int mode) {
        if(mode != this.mode) {
            this.mode = mode;
            this.applyModeAndGravity();
        }

    }

    public int getTabMode() {
        return this.mode;
    }

    public void setTabGravity(int gravity) {
        if(this.tabGravity != gravity) {
            this.tabGravity = gravity;
            this.applyModeAndGravity();
        }

    }

    public int getTabGravity() {
        return this.tabGravity;
    }

    public void setSelectedTabIndicatorGravity(int indicatorGravity) {
        if(this.tabIndicatorGravity != indicatorGravity) {
            this.tabIndicatorGravity = indicatorGravity;
            ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
        }

    }

    public int getTabIndicatorGravity() {
        return this.tabIndicatorGravity;
    }

    public void setTabIndicatorFullWidth(boolean tabIndicatorFullWidth) {
        this.tabIndicatorFullWidth = tabIndicatorFullWidth;
        ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
    }

    public boolean isTabIndicatorFullWidth() {
        return this.tabIndicatorFullWidth;
    }

    public void setInlineLabel(boolean inline) {
        if(this.inlineLabel != inline) {
            this.inlineLabel = inline;

            for(int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                View child = this.slidingTabIndicator.getChildAt(i);
                if(child instanceof CustomTabLayout.TabView) {
                    ((CustomTabLayout.TabView)child).updateOrientation();
                }
            }

            this.applyModeAndGravity();
        }

    }

    public void setInlineLabelResource(@BoolRes int inlineResourceId) {
        this.setInlineLabel(this.getResources().getBoolean(inlineResourceId));
    }

    public boolean isInlineLabel() {
        return this.inlineLabel;
    }

    public void setUnboundedRipple(boolean unboundedRipple) {
        if(this.unboundedRipple != unboundedRipple) {
            this.unboundedRipple = unboundedRipple;

            for(int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                View child = this.slidingTabIndicator.getChildAt(i);
                if(child instanceof CustomTabLayout.TabView) {
                    ((CustomTabLayout.TabView)child).updateBackgroundDrawable(this.getContext());
                }
            }
        }

    }

    public void setUnboundedRippleResource(@BoolRes int unboundedRippleResourceId) {
        this.setUnboundedRipple(this.getResources().getBoolean(unboundedRippleResourceId));
    }

    public boolean hasUnboundedRipple() {
        return this.unboundedRipple;
    }

    public void setTabTextColors(@Nullable ColorStateList textColor) {
        if(this.tabTextColors != textColor) {
            this.tabTextColors = textColor;
            this.updateAllTabs();
        }

    }

    @Nullable
    public ColorStateList getTabTextColors() {
        return this.tabTextColors;
    }

    public void setTabTextColors(int normalColor, int selectedColor) {
        this.setTabTextColors(createColorStateList(normalColor, selectedColor));
    }

    public void setTabIconTint(@Nullable ColorStateList iconTint) {
        if(this.tabIconTint != iconTint) {
            this.tabIconTint = iconTint;
            this.updateAllTabs();
        }

    }

    public void setTabIconTintResource(@ColorRes int iconTintResourceId) {
        this.setTabIconTint(AppCompatResources.getColorStateList(this.getContext(), iconTintResourceId));
    }

    @Nullable
    public ColorStateList getTabIconTint() {
        return this.tabIconTint;
    }

    @Nullable
    public ColorStateList getTabRippleColor() {
        return this.tabRippleColorStateList;
    }

    public void setTabRippleColor(@Nullable ColorStateList color) {
        if(this.tabRippleColorStateList != color) {
            this.tabRippleColorStateList = color;

            for(int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                View child = this.slidingTabIndicator.getChildAt(i);
                if(child instanceof CustomTabLayout.TabView) {
                    ((CustomTabLayout.TabView)child).updateBackgroundDrawable(this.getContext());
                }
            }
        }

    }

    public void setTabRippleColorResource(@ColorRes int tabRippleColorResourceId) {
        this.setTabRippleColor(AppCompatResources.getColorStateList(this.getContext(), tabRippleColorResourceId));
    }

    @Nullable
    public Drawable getTabSelectedIndicator() {
        return this.tabSelectedIndicator;
    }

    public void setSelectedTabIndicator(@Nullable Drawable tabSelectedIndicator) {
        if(this.tabSelectedIndicator != tabSelectedIndicator) {
            this.tabSelectedIndicator = tabSelectedIndicator;
            ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
        }

    }

    public void setSelectedTabIndicator(@DrawableRes int tabSelectedIndicatorResourceId) {
        if(tabSelectedIndicatorResourceId != 0) {
            this.setSelectedTabIndicator(AppCompatResources.getDrawable(this.getContext(), tabSelectedIndicatorResourceId));
        } else {
            this.setSelectedTabIndicator((Drawable)null);
        }

    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        this.setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager, boolean autoRefresh) {
        this.setupWithViewPager(viewPager, autoRefresh, false);
    }

    private void setupWithViewPager(@Nullable ViewPager viewPager, boolean autoRefresh, boolean implicitSetup) {
        if(this.viewPager != null) {
            if(this.pageChangeListener != null) {
                this.viewPager.removeOnPageChangeListener(this.pageChangeListener);
            }

            if(this.adapterChangeListener != null) {
                this.viewPager.removeOnAdapterChangeListener(this.adapterChangeListener);
            }
        }

        if(this.currentVpSelectedListener != null) {
            this.removeOnTabSelectedListener(this.currentVpSelectedListener);
            this.currentVpSelectedListener = null;
        }

        if(viewPager != null) {
            this.viewPager = viewPager;
            if(this.pageChangeListener == null) {
                this.pageChangeListener = new CustomTabLayout.TabLayoutOnPageChangeListener(this);
            }

            this.pageChangeListener.reset();
            viewPager.addOnPageChangeListener(this.pageChangeListener);
            this.currentVpSelectedListener = new CustomTabLayout.ViewPagerOnTabSelectedListener(viewPager);
            this.addOnTabSelectedListener(this.currentVpSelectedListener);
            PagerAdapter adapter = viewPager.getAdapter();
            if(adapter != null) {
                this.setPagerAdapter(adapter, autoRefresh);
            }

            if(this.adapterChangeListener == null) {
                this.adapterChangeListener = new CustomTabLayout.AdapterChangeListener();
            }

            this.adapterChangeListener.setAutoRefresh(autoRefresh);
            viewPager.addOnAdapterChangeListener(this.adapterChangeListener);
            this.setScrollPosition(viewPager.getCurrentItem(), 0.0F, true);
        } else {
            this.viewPager = null;
            this.setPagerAdapter((PagerAdapter)null, false);
        }

        this.setupViewPagerImplicitly = implicitSetup;
    }

    /** @deprecated */
    @Deprecated
    public void setTabsFromPagerAdapter(@Nullable PagerAdapter adapter) {
        this.setPagerAdapter(adapter, false);
    }

    public boolean shouldDelayChildPressedState() {
        return this.getTabScrollRange() > 0;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(this.viewPager == null) {
            ViewParent vp = this.getParent();
            if(vp instanceof ViewPager) {
                this.setupWithViewPager((ViewPager)vp, true, true);
            }
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(this.setupViewPagerImplicitly) {
            this.setupWithViewPager((ViewPager)null);
            this.setupViewPagerImplicitly = false;
        }

    }

    private int getTabScrollRange() {
        return Math.max(0, this.slidingTabIndicator.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
    }

    void setPagerAdapter(@Nullable PagerAdapter adapter, boolean addObserver) {
        if(this.pagerAdapter != null && this.pagerAdapterObserver != null) {
            this.pagerAdapter.unregisterDataSetObserver(this.pagerAdapterObserver);
        }

        this.pagerAdapter = adapter;
        if(addObserver && adapter != null) {
            if(this.pagerAdapterObserver == null) {
                this.pagerAdapterObserver = new CustomTabLayout.PagerAdapterObserver();
            }

            adapter.registerDataSetObserver(this.pagerAdapterObserver);
        }

        this.populateFromPagerAdapter();
    }

    void populateFromPagerAdapter() {
        this.removeAllTabs();
        if(this.pagerAdapter != null) {
            int adapterCount = this.pagerAdapter.getCount();

            int curItem;
            for(curItem = 0; curItem < adapterCount; ++curItem) {
                this.addTab(this.newTab().setText(this.pagerAdapter.getPageTitle(curItem)), false);
            }

            if(this.viewPager != null && adapterCount > 0) {
                curItem = this.viewPager.getCurrentItem();
                if(curItem != this.getSelectedTabPosition() && curItem < this.getTabCount()) {
                    this.selectTab(this.getTabAt(curItem));
                }
            }
        }

    }

    private void updateAllTabs() {
        int i = 0;

        for(int z = this.tabs.size(); i < z; ++i) {
            ((CustomTabLayout.Tab)this.tabs.get(i)).updateView();
        }

    }

    private CustomTabLayout.TabView createTabView(@NonNull CustomTabLayout.Tab tab) {
        CustomTabLayout.TabView tabView = this.tabViewPool != null?(CustomTabLayout.TabView)this.tabViewPool.acquire():null;
        if(tabView == null) {
            tabView = new CustomTabLayout.TabView(this.getContext());
        }

        tabView.setTab(tab);
        tabView.setFocusable(true);
        tabView.setMinimumWidth(this.getTabMinWidth());
        if(TextUtils.isEmpty(tab.contentDesc)) {
            tabView.setContentDescription(tab.text);
        } else {
            tabView.setContentDescription(tab.contentDesc);
        }

        return tabView;
    }

    private void configureTab(CustomTabLayout.Tab tab, int position) {
        tab.setPosition(position);
        this.tabs.add(position, tab);
        int count = this.tabs.size();

        for(int i = position + 1; i < count; ++i) {
            ((CustomTabLayout.Tab)this.tabs.get(i)).setPosition(i);
        }

    }

    private void addTabView(CustomTabLayout.Tab tab) {
        CustomTabLayout.TabView tabView = tab.view;
        this.slidingTabIndicator.addView(tabView, tab.getPosition(), this.createLayoutParamsForTabs());
    }

    public void addView(View child) {
        this.addViewInternal(child);
    }

    public void addView(View child, int index) {
        this.addViewInternal(child);
    }

    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        this.addViewInternal(child);
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        this.addViewInternal(child);
    }

    private void addViewInternal(View child) {
        if(child instanceof TabItem) {
            this.addTabFromItemView((TabItem)child);
        } else {
            throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
        }
    }

    private android.widget.LinearLayout.LayoutParams createLayoutParamsForTabs() {
        android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(-2, -1);
        lp.setMargins(20,20,20,20);
        this.updateTabViewLayoutParams(lp);
        return lp;
    }

    private void updateTabViewLayoutParams(android.widget.LinearLayout.LayoutParams lp) {
        if(this.mode == 1 && this.tabGravity == 0) {
            lp.width = 0;
            lp.weight = 1.0F;
        } else {
            lp.width = -2;
            lp.weight = 0.0F;
        }

    }

    @Dimension(
            unit = 1
    )
    int dpToPx(@Dimension(unit = 0) int dps) {
        return Math.round(this.getResources().getDisplayMetrics().density * (float)dps);
    }

    protected void onDraw(Canvas canvas) {
        for(int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            View tabView = this.slidingTabIndicator.getChildAt(i);
            if(tabView instanceof CustomTabLayout.TabView) {
                ((CustomTabLayout.TabView)tabView).drawBackground(canvas);
            }
        }

        super.onDraw(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int idealHeight = this.dpToPx(this.getDefaultHeight()) + this.getPaddingTop() + this.getPaddingBottom();
        switch(MeasureSpec.getMode(heightMeasureSpec)) {
            case -2147483648:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)), 1073741824);
                break;
            case 0:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, 1073741824);
        }

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        if(MeasureSpec.getMode(widthMeasureSpec) != 0) {
            this.tabMaxWidth = this.requestedTabMaxWidth > 0?this.requestedTabMaxWidth:specWidth - this.dpToPx(56);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(this.getChildCount() == 1) {
            View child = this.getChildAt(0);
            boolean remeasure = false;
            switch(this.mode) {
                case 0:
                    remeasure = child.getMeasuredWidth() < this.getMeasuredWidth();
                    break;
                case 1:
                    remeasure = child.getMeasuredWidth() != this.getMeasuredWidth();
            }

            if(remeasure) {
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), child.getLayoutParams().height);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

    }

    private void removeTabViewAt(int position) {
        CustomTabLayout.TabView view = (CustomTabLayout.TabView)this.slidingTabIndicator.getChildAt(position);
        this.slidingTabIndicator.removeViewAt(position);
        if(view != null) {
            view.reset();
            this.tabViewPool.release(view);
        }

        this.requestLayout();
    }

    private void animateToTab(int newPosition) {
        if(newPosition != -1) {
            if(this.getWindowToken() != null && ViewCompat.isLaidOut(this) && !this.slidingTabIndicator.childrenNeedLayout()) {
                int startScrollX = this.getScrollX();
                int targetScrollX = this.calculateScrollXForTab(newPosition, 0.0F);
                if(startScrollX != targetScrollX) {
                    this.ensureScrollAnimator();
                    this.scrollAnimator.setIntValues(new int[]{startScrollX, targetScrollX});
                    this.scrollAnimator.start();
                }

                this.slidingTabIndicator.animateIndicatorToPosition(newPosition, this.tabIndicatorAnimationDuration);
            } else {
                this.setScrollPosition(newPosition, 0.0F, true);
            }
        }
    }

    private void ensureScrollAnimator() {
        if(this.scrollAnimator == null) {
            this.scrollAnimator = new ValueAnimator();
            this.scrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.scrollAnimator.setDuration((long)this.tabIndicatorAnimationDuration);
            this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    CustomTabLayout.this.scrollTo(((Integer)animator.getAnimatedValue()).intValue(), 0);
                }
            });
        }

    }

    void setScrollAnimatorListener(Animator.AnimatorListener listener) {
        this.ensureScrollAnimator();
        this.scrollAnimator.addListener(listener);
    }

    private void setSelectedTabView(int position) {
        int tabCount = this.slidingTabIndicator.getChildCount();
        if(position < tabCount) {
            for(int i = 0; i < tabCount; ++i) {
                View child = this.slidingTabIndicator.getChildAt(i);
                child.setSelected(i == position);
                child.setActivated(i == position);
            }
        }

    }

    void selectTab(CustomTabLayout.Tab tab) {
        this.selectTab(tab, true);
    }

    void selectTab(CustomTabLayout.Tab tab, boolean updateIndicator) {
        CustomTabLayout.Tab currentTab = this.selectedTab;
        if(currentTab == tab) {
            if(currentTab != null) {
                this.dispatchTabReselected(tab);
                this.animateToTab(tab.getPosition());
            }
        } else {
            int newPosition = tab != null?tab.getPosition():-1;
            if(updateIndicator) {
                if((currentTab == null || currentTab.getPosition() == -1) && newPosition != -1) {
                    this.setScrollPosition(newPosition, 0.0F, true);
                } else {
                    this.animateToTab(newPosition);
                }

                if(newPosition != -1) {
                    this.setSelectedTabView(newPosition);
                }
            }

            this.selectedTab = tab;
            if(currentTab != null) {
                this.dispatchTabUnselected(currentTab);
            }

            if(tab != null) {
                this.dispatchTabSelected(tab);
            }
        }

    }

    private void dispatchTabSelected(@NonNull CustomTabLayout.Tab tab) {
        for(int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            ((CustomTabLayout.BaseOnTabSelectedListener)this.selectedListeners.get(i)).onTabSelected(tab);
        }

    }

    private void dispatchTabUnselected(@NonNull CustomTabLayout.Tab tab) {
        for(int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            ((CustomTabLayout.BaseOnTabSelectedListener)this.selectedListeners.get(i)).onTabUnselected(tab);
        }

    }

    private void dispatchTabReselected(@NonNull CustomTabLayout.Tab tab) {
        for(int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            ((CustomTabLayout.BaseOnTabSelectedListener)this.selectedListeners.get(i)).onTabReselected(tab);
        }

    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        if(this.mode == 0) {
            View selectedChild = this.slidingTabIndicator.getChildAt(position);
            View nextChild = position + 1 < this.slidingTabIndicator.getChildCount()?this.slidingTabIndicator.getChildAt(position + 1):null;
            int selectedWidth = selectedChild != null?selectedChild.getWidth():0;
            int nextWidth = nextChild != null?nextChild.getWidth():0;
            int scrollBase = selectedChild.getLeft() + selectedWidth / 2 - this.getWidth() / 2;
            int scrollOffset = (int)((float)(selectedWidth + nextWidth) * 0.5F * positionOffset);
            return ViewCompat.getLayoutDirection(this) == 0?scrollBase + scrollOffset:scrollBase - scrollOffset;
        } else {
            return 0;
        }
    }

    private void applyModeAndGravity() {
        int paddingStart = 0;
        if(this.mode == 0) {
            paddingStart = Math.max(0, this.contentInsetStart - this.tabPaddingStart);
        }

        ViewCompat.setPaddingRelative(this.slidingTabIndicator, paddingStart, 0, 0, 0);
        switch(this.mode) {
            case 0:
                this.slidingTabIndicator.setGravity(8388611);
                break;
            case 1:
                this.slidingTabIndicator.setGravity(1);
        }

        this.updateTabViews(true);
    }

    void updateTabViews(boolean requestLayout) {
        for(int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            View child = this.slidingTabIndicator.getChildAt(i);
            child.setMinimumWidth(this.getTabMinWidth());
            this.updateTabViewLayoutParams((android.widget.LinearLayout.LayoutParams)child.getLayoutParams());
            if(requestLayout) {
                child.requestLayout();
            }
        }

    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        int[][] states = new int[2][];
        int[] colors = new int[2];
        int i = 0;
        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i = i + 1;
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        ++i;
        return new ColorStateList(states, colors);
    }

    @Dimension(
            unit = 0
    )
    private int getDefaultHeight() {
        boolean hasIconAndText = false;
        int i = 0;

        for(int count = this.tabs.size(); i < count; ++i) {
            CustomTabLayout.Tab tab = (CustomTabLayout.Tab)this.tabs.get(i);
            if(tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                hasIconAndText = true;
                break;
            }
        }

        return hasIconAndText && !this.inlineLabel?72:48;
    }

    private int getTabMinWidth() {
        return this.requestedTabMinWidth != -1?this.requestedTabMinWidth:(this.mode == 0?this.scrollableTabMinWidth:0);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return this.generateDefaultLayoutParams();
    }

    int getTabMaxWidth() {
        return this.tabMaxWidth;
    }


    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean autoRefresh;

        AdapterChangeListener() {
        }

        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            if(CustomTabLayout.this.viewPager == viewPager) {
                CustomTabLayout.this.setPagerAdapter(newAdapter, this.autoRefresh);
            }

        }

        void setAutoRefresh(boolean autoRefresh) {
            this.autoRefresh = autoRefresh;
        }
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        public void onChanged() {
            CustomTabLayout.this.populateFromPagerAdapter();
        }

        public void onInvalidated() {
            CustomTabLayout.this.populateFromPagerAdapter();
        }
    }

    public static class ViewPagerOnTabSelectedListener implements CustomTabLayout.OnTabSelectedListener {
        private final ViewPager viewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        public void onTabSelected(CustomTabLayout.Tab tab) {
            this.viewPager.setCurrentItem(tab.getPosition());
        }

        public void onTabUnselected(CustomTabLayout.Tab tab) {
        }

        public void onTabReselected(CustomTabLayout.Tab tab) {
        }
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<CustomTabLayout> tabLayoutRef;
        private int previousScrollState;
        private int scrollState;

        public TabLayoutOnPageChangeListener(CustomTabLayout tabLayout) {
            this.tabLayoutRef = new WeakReference(tabLayout);
        }

        public void onPageScrollStateChanged(int state) {
            this.previousScrollState = this.scrollState;
            this.scrollState = state;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            CustomTabLayout tabLayout = (CustomTabLayout)this.tabLayoutRef.get();
            if(tabLayout != null) {
                boolean updateText = this.scrollState != 2 || this.previousScrollState == 1;
                boolean updateIndicator = this.scrollState != 2 || this.previousScrollState != 0;
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator);
            }

        }

        public void onPageSelected(int position) {
            CustomTabLayout tabLayout = (CustomTabLayout)this.tabLayoutRef.get();
            if(tabLayout != null && tabLayout.getSelectedTabPosition() != position && position < tabLayout.getTabCount()) {
                boolean updateIndicator = this.scrollState == 0 || this.scrollState == 2 && this.previousScrollState == 0;
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator);
            }

        }

        void reset() {
            this.previousScrollState = this.scrollState = 0;
        }
    }

    private class SlidingTabIndicator extends LinearLayout {
        private int selectedIndicatorHeight;
        private final Paint selectedIndicatorPaint;
        private final GradientDrawable defaultSelectionIndicator;
        int selectedPosition = -1;
        float selectionOffset;
        private int layoutDirection = -1;
        private int indicatorLeft = -1;
        private int indicatorRight = -1;
        private ValueAnimator indicatorAnimator;

        SlidingTabIndicator(Context context) {
            super(context);
            this.setWillNotDraw(false);
            this.selectedIndicatorPaint = new Paint();
            this.defaultSelectionIndicator = new GradientDrawable();
        }

        void setSelectedIndicatorColor(int color) {
            if(this.selectedIndicatorPaint.getColor() != color) {
                this.selectedIndicatorPaint.setColor(color);
                ViewCompat.postInvalidateOnAnimation(this);
            }

        }

        void setSelectedIndicatorHeight(int height) {
            if(this.selectedIndicatorHeight != height) {
                this.selectedIndicatorHeight = height;
                ViewCompat.postInvalidateOnAnimation(this);
            }

        }

        boolean childrenNeedLayout() {
            int i = 0;

            for(int z = this.getChildCount(); i < z; ++i) {
                View child = this.getChildAt(i);
                if(child.getWidth() <= 0) {
                    return true;
                }
            }

            return false;
        }

        void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
            if(this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }

            this.selectedPosition = position;
            this.selectionOffset = positionOffset;
            this.updateIndicatorPosition();
        }

        float getIndicatorPosition() {
            return (float)this.selectedPosition + this.selectionOffset;
        }

        public void onRtlPropertiesChanged(int layoutDirection) {
            super.onRtlPropertiesChanged(layoutDirection);
            if(Build.VERSION.SDK_INT < 23 && this.layoutDirection != layoutDirection) {
                this.requestLayout();
                this.layoutDirection = layoutDirection;
            }

        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if(MeasureSpec.getMode(widthMeasureSpec) == 1073741824) {
                if(CustomTabLayout.this.mode == 1 && CustomTabLayout.this.tabGravity == 1) {
                    int count = this.getChildCount();
                    int largestTabWidth = 0;
                    int gutter = 0;

                    for(int z = count; gutter < z; ++gutter) {
                        View child = this.getChildAt(gutter);
                        if(child.getVisibility() == 0) {
                            largestTabWidth = Math.max(largestTabWidth, child.getMeasuredWidth());
                        }
                    }

                    if(largestTabWidth <= 0) {
                        return;
                    }

                    gutter = CustomTabLayout.this.dpToPx(16);
                    boolean remeasure = false;
                    if(largestTabWidth * count > this.getMeasuredWidth() - gutter * 2) {
                        CustomTabLayout.this.tabGravity = 0;
                        CustomTabLayout.this.updateTabViews(false);
                        remeasure = true;
                    } else {
                        for(int i = 0; i < count; ++i) {
                            android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams)this.getChildAt(i).getLayoutParams();
                            if(lp.width != largestTabWidth || lp.weight != 0.0F) {
                                lp.width = largestTabWidth;
                                lp.weight = 0.0F;
                                remeasure = true;
                            }
                        }
                    }

                    if(remeasure) {
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                }

            }
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if(this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
                long duration = this.indicatorAnimator.getDuration();
                this.animateIndicatorToPosition(this.selectedPosition, Math.round((1.0F - this.indicatorAnimator.getAnimatedFraction()) * (float)duration));
            } else {
                this.updateIndicatorPosition();
            }

        }

        private void updateIndicatorPosition() {
            View selectedTitle = this.getChildAt(this.selectedPosition);
            int left;
            int right;
            if(selectedTitle != null && selectedTitle.getWidth() > 0) {
                left = selectedTitle.getLeft();
                right = selectedTitle.getRight();
                if(!CustomTabLayout.this.tabIndicatorFullWidth && selectedTitle instanceof CustomTabLayout.TabView) {
                    this.calculateTabViewContentBounds((CustomTabLayout.TabView)selectedTitle, CustomTabLayout.this.tabViewContentBounds);
                    left = (int) CustomTabLayout.this.tabViewContentBounds.left;
                    right = (int) CustomTabLayout.this.tabViewContentBounds.right;
                }

                if(this.selectionOffset > 0.0F && this.selectedPosition < this.getChildCount() - 1) {
                    View nextTitle = this.getChildAt(this.selectedPosition + 1);
                    int nextTitleLeft = nextTitle.getLeft();
                    int nextTitleRight = nextTitle.getRight();
                    if(!CustomTabLayout.this.tabIndicatorFullWidth && nextTitle instanceof CustomTabLayout.TabView) {
                        this.calculateTabViewContentBounds((CustomTabLayout.TabView)nextTitle, CustomTabLayout.this.tabViewContentBounds);
                        nextTitleLeft = (int) CustomTabLayout.this.tabViewContentBounds.left;
                        nextTitleRight = (int) CustomTabLayout.this.tabViewContentBounds.right;
                    }

                    left = (int)(this.selectionOffset * (float)nextTitleLeft + (1.0F - this.selectionOffset) * (float)left);
                    right = (int)(this.selectionOffset * (float)nextTitleRight + (1.0F - this.selectionOffset) * (float)right);
                }
            } else {
                right = -1;
                left = -1;
            }

            this.setIndicatorPosition(left, right);
        }

        void setIndicatorPosition(int left, int right) {
            if(left != this.indicatorLeft || right != this.indicatorRight) {
                this.indicatorLeft = left;
                this.indicatorRight = right;
                ViewCompat.postInvalidateOnAnimation(this);
            }

        }

        void animateIndicatorToPosition(final int position, int duration) {
            if(this.indicatorAnimator != null && this.indicatorAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }

            View targetView = this.getChildAt(position);
            if(targetView == null) {
                this.updateIndicatorPosition();
            } else {
                int targetLeft = targetView.getLeft();
                int targetRight = targetView.getRight();
                if(!CustomTabLayout.this.tabIndicatorFullWidth && targetView instanceof CustomTabLayout.TabView) {
                    this.calculateTabViewContentBounds((CustomTabLayout.TabView)targetView, CustomTabLayout.this.tabViewContentBounds);
                    targetLeft = (int) CustomTabLayout.this.tabViewContentBounds.left;
                    targetRight = (int) CustomTabLayout.this.tabViewContentBounds.right;
                }

                final int startLeft = this.indicatorLeft;
                final int startRight = this.indicatorRight;
                if(startLeft != targetLeft || startRight != targetRight) {
                    ValueAnimator animator = this.indicatorAnimator = new ValueAnimator();
                    animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                    animator.setDuration((long)duration);
                    animator.setFloatValues(new float[]{0.0F, 1.0F});
                    final int finalTargetLeft = targetLeft;
                    final int finalTargetRight = targetRight;
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @SuppressLint("RestrictedApi")
                        public void onAnimationUpdate(ValueAnimator animator) {
                            float fraction = animator.getAnimatedFraction();
                            CustomTabLayout.SlidingTabIndicator.this.setIndicatorPosition(AnimationUtils.lerp(startLeft, finalTargetLeft, fraction), AnimationUtils.lerp(startRight, finalTargetRight, fraction));
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            CustomTabLayout.SlidingTabIndicator.this.selectedPosition = position;
                            CustomTabLayout.SlidingTabIndicator.this.selectionOffset = 0.0F;
                        }
                    });
                    animator.start();
                }

            }
        }

        private void calculateTabViewContentBounds(CustomTabLayout.TabView tabView, RectF contentBounds) {
            int tabViewContentWidth = tabView.getContentWidth();
            if(tabViewContentWidth < CustomTabLayout.this.dpToPx(24)) {
                tabViewContentWidth = CustomTabLayout.this.dpToPx(24);
            }

            int tabViewCenter = (tabView.getLeft() + tabView.getRight()) / 2;
            int contentLeftBounds = tabViewCenter - tabViewContentWidth / 2;
            int contentRightBounds = tabViewCenter + tabViewContentWidth / 2;
            contentBounds.set((float)contentLeftBounds, 0.0F, (float)contentRightBounds, 0.0F);
        }

        public void draw(Canvas canvas) {
            int indicatorHeight = 0;
            if(CustomTabLayout.this.tabSelectedIndicator != null) {
                indicatorHeight = CustomTabLayout.this.tabSelectedIndicator.getIntrinsicHeight();
            }

            if(this.selectedIndicatorHeight >= 0) {
                indicatorHeight = this.selectedIndicatorHeight;
            }

            int indicatorTop = 0;
            int indicatorBottom = 0;
            switch(CustomTabLayout.this.tabIndicatorGravity) {
                case 0:
                    indicatorTop = this.getHeight() - indicatorHeight;
                    indicatorBottom = this.getHeight();
                    break;
                case 1:
                    indicatorTop = (this.getHeight() - indicatorHeight) / 2;
                    indicatorBottom = (this.getHeight() + indicatorHeight) / 2;
                    break;
                case 2:
                    indicatorTop = 0;
                    indicatorBottom = indicatorHeight;
                    break;
                case 3:
                    indicatorTop = 0;
                    indicatorBottom = this.getHeight();
            }

            if(this.indicatorLeft >= 0 && this.indicatorRight > this.indicatorLeft) {
                Drawable selectedIndicator = DrawableCompat.wrap((Drawable)(CustomTabLayout.this.tabSelectedIndicator != null? CustomTabLayout.this.tabSelectedIndicator:this.defaultSelectionIndicator));
                selectedIndicator.setBounds(this.indicatorLeft, indicatorTop, this.indicatorRight, indicatorBottom);
                if(this.selectedIndicatorPaint != null) {
                    if(Build.VERSION.SDK_INT == 21) {
                        selectedIndicator.setColorFilter(this.selectedIndicatorPaint.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else {
                        DrawableCompat.setTint(selectedIndicator, this.selectedIndicatorPaint.getColor());
                    }
                }

                selectedIndicator.draw(canvas);
            }

            super.draw(canvas);
        }
    }

   class TabView extends LinearLayout {
        private CustomTabLayout.Tab tab;
        private TextView textView;
        private ImageView iconView;
        private View customView;
        private TextView customTextView;
        private ImageView customIconView;

        @Nullable
        public Drawable getBaseBackgroundDrawable() {
            return baseBackgroundDrawable;
        }

        public void setBaseBackgroundDrawable(@Nullable Drawable baseBackgroundDrawable) {
            this.baseBackgroundDrawable = baseBackgroundDrawable;
        }

        @Nullable
        private Drawable baseBackgroundDrawable;
        private int defaultMaxLines = 2;

        public TabView(Context context) {
            super(context);
            this.updateBackgroundDrawable(context);
            ViewCompat.setPaddingRelative(this, CustomTabLayout.this.tabPaddingStart, CustomTabLayout.this.tabPaddingTop, CustomTabLayout.this.tabPaddingEnd, CustomTabLayout.this.tabPaddingBottom);

            this.setGravity(17);
            this.setOrientation(CustomTabLayout.this.inlineLabel?0:1);
            this.setClickable(true);
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
        }

        private void updateBackgroundDrawable(Context context) {
            if(CustomTabLayout.this.tabBackgroundResId != 0) {
                this.baseBackgroundDrawable = AppCompatResources.getDrawable(context, CustomTabLayout.this.tabBackgroundResId);
                if(this.baseBackgroundDrawable != null && this.baseBackgroundDrawable.isStateful()) {
                    this.baseBackgroundDrawable.setState(this.getDrawableState());
                }
            } else {
                this.baseBackgroundDrawable = null;
            }

            Drawable contentDrawable = new GradientDrawable();
            ((GradientDrawable)contentDrawable).setColor(0);
            Object background;
            if(CustomTabLayout.this.tabRippleColorStateList != null) {
                GradientDrawable maskDrawable = new GradientDrawable();
                maskDrawable.setCornerRadius(1.0E-5F);
                maskDrawable.setColor(-1);
                ColorStateList rippleColor = RippleUtils.convertToRippleDrawableColor(CustomTabLayout.this.tabRippleColorStateList);
                if(Build.VERSION.SDK_INT >= 21) {
                    background = new RippleDrawable(rippleColor, CustomTabLayout.this.unboundedRipple?null:contentDrawable, CustomTabLayout.this.unboundedRipple?null:maskDrawable);
                } else {
                    Drawable rippleDrawable = DrawableCompat.wrap(maskDrawable);
                    DrawableCompat.setTintList(rippleDrawable, rippleColor);
                    background = new LayerDrawable(new Drawable[]{contentDrawable, rippleDrawable});
                }
            } else {
                background = contentDrawable;
            }

            ViewCompat.setBackground(this, (Drawable)background);
            CustomTabLayout.this.invalidate();
        }

        private void drawBackground(Canvas canvas) {
            if(this.baseBackgroundDrawable != null) {
                this.baseBackgroundDrawable.setBounds(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
                this.baseBackgroundDrawable.draw(canvas);
            }

        }

        protected void drawableStateChanged() {
            super.drawableStateChanged();
            boolean changed = false;
            int[] state = this.getDrawableState();
            if(this.baseBackgroundDrawable != null && this.baseBackgroundDrawable.isStateful()) {
                changed |= this.baseBackgroundDrawable.setState(state);
            }

            if(changed) {
                this.invalidate();
                CustomTabLayout.this.invalidate();
            }

        }

        public boolean performClick() {
            boolean handled = super.performClick();
            if(this.tab != null) {
                if(!handled) {
                    this.playSoundEffect(0);
                }

                this.tab.select();
                return true;
            } else {
                return handled;
            }
        }

        public void setSelected(boolean selected) {
            boolean changed = this.isSelected() != selected;
            super.setSelected(selected);
            if(changed && selected && Build.VERSION.SDK_INT < 16) {
                this.sendAccessibilityEvent(4);
            }

            if(this.textView != null) {
                this.textView.setSelected(selected);
            }

            if(this.iconView != null) {
                this.iconView.setSelected(selected);
            }

            if(this.customView != null) {
                this.customView.setSelected(selected);
            }

        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            event.setClassName(ActionBar.Tab.class.getName());
        }

        @TargetApi(14)
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName(ActionBar.Tab.class.getName());
        }

        public void onMeasure(int origWidthMeasureSpec, int origHeightMeasureSpec) {
            int specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec);
            int specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec);
            int maxWidth = CustomTabLayout.this.getTabMaxWidth();
            int widthMeasureSpec;
            if(maxWidth <= 0 || specWidthMode != 0 && specWidthSize <= maxWidth) {
                widthMeasureSpec = origWidthMeasureSpec;
            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(CustomTabLayout.this.tabMaxWidth, -2147483648);
            }

            super.onMeasure(widthMeasureSpec, origHeightMeasureSpec);
            if(this.textView != null) {
                float textSize = CustomTabLayout.this.tabTextSize;
                int maxLines = this.defaultMaxLines;
                if(this.iconView != null && this.iconView.getVisibility() == 0) {
                    maxLines = 1;
                } else if(this.textView != null && this.textView.getLineCount() > 1) {
                    textSize = CustomTabLayout.this.tabTextMultiLineSize;
                }

                float curTextSize = this.textView.getTextSize();
                int curLineCount = this.textView.getLineCount();
                int curMaxLines = TextViewCompat.getMaxLines(this.textView);
                if(textSize != curTextSize || curMaxLines >= 0 && maxLines != curMaxLines) {
                    boolean updateTextView = true;
                    if(CustomTabLayout.this.mode == 1 && textSize > curTextSize && curLineCount == 1) {
                        Layout layout = this.textView.getLayout();
                        if(layout == null || this.approximateLineWidth(layout, 0, textSize) > (float)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight())) {
                            updateTextView = false;
                        }
                    }

                    if(updateTextView) {
                        this.textView.setTextSize(0, textSize);
                        this.textView.setMaxLines(maxLines);
                        super.onMeasure(widthMeasureSpec, origHeightMeasureSpec);
                    }
                }
            }

        }

        void setTab(@Nullable CustomTabLayout.Tab tab) {
            if(tab != this.tab) {
                this.tab = tab;
                this.update();
            }

        }

        void reset() {
            this.setTab((CustomTabLayout.Tab)null);
            this.setSelected(false);
        }

        final void update() {
            CustomTabLayout.Tab tab = this.tab;
            View custom = tab != null?tab.getCustomView():null;
            if(custom != null) {
                ViewParent customParent = custom.getParent();
                if(customParent != this) {
                    if(customParent != null) {
                        ((ViewGroup)customParent).removeView(custom);
                    }

                    this.addView(custom);
                }

                this.customView = custom;
                if(this.textView != null) {
                    this.textView.setVisibility(8);
                }

                if(this.iconView != null) {
                    this.iconView.setVisibility(8);
                    this.iconView.setImageDrawable((Drawable)null);
                }

                this.customTextView = (TextView)custom.findViewById(16908308);
                if(this.customTextView != null) {
                    this.defaultMaxLines = TextViewCompat.getMaxLines(this.customTextView);
                }

                this.customIconView = (ImageView)custom.findViewById(16908294);
            } else {
                if(this.customView != null) {
                    this.removeView(this.customView);
                    this.customView = null;
                }

                this.customTextView = null;
                this.customIconView = null;
            }

            if(this.customView == null) {
                if(this.iconView == null) {
                    ImageView iconView = (ImageView) LayoutInflater.from(this.getContext()).inflate(R.layout.design_layout_tab_icon, this, false);
                    this.addView(iconView, 0);
                    this.iconView = iconView;
                }

                Drawable icon = tab != null && tab.getIcon() != null?DrawableCompat.wrap(tab.getIcon()).mutate():null;
                if(icon != null) {
                    DrawableCompat.setTintList(icon, CustomTabLayout.this.tabIconTint);
                    if(CustomTabLayout.this.tabIconTintMode != null) {
                        DrawableCompat.setTintMode(icon, CustomTabLayout.this.tabIconTintMode);
                    }
                }

                if(this.textView == null) {
                    TextView textView = (TextView)LayoutInflater.from(this.getContext()).inflate(R.layout.design_layout_tab_text, this, false);
                    this.addView(textView);
                    this.textView = textView;
                    this.defaultMaxLines = TextViewCompat.getMaxLines(this.textView);
                }

                TextViewCompat.setTextAppearance(this.textView, CustomTabLayout.this.tabTextAppearance);
                if(CustomTabLayout.this.tabTextColors != null) {
                    this.textView.setTextColor(CustomTabLayout.this.tabTextColors);
                }

                this.updateTextAndIcon(this.textView, this.iconView);
            } else if(this.customTextView != null || this.customIconView != null) {
                this.updateTextAndIcon(this.customTextView, this.customIconView);
            }

            if(tab != null && !TextUtils.isEmpty(tab.contentDesc)) {
                this.setContentDescription(tab.contentDesc);
            }

            this.setSelected(tab != null && tab.isSelected());
        }

        final void updateOrientation() {
            this.setOrientation(CustomTabLayout.this.inlineLabel?0:1);
            if(this.customTextView == null && this.customIconView == null) {
                this.updateTextAndIcon(this.textView, this.iconView);
            } else {
                this.updateTextAndIcon(this.customTextView, this.customIconView);
            }

        }

        private void updateTextAndIcon(@Nullable TextView textView, @Nullable ImageView iconView) {
            Drawable icon = this.tab != null && this.tab.getIcon() != null?DrawableCompat.wrap(this.tab.getIcon()).mutate():null;
            CharSequence text = this.tab != null?this.tab.getText():null;
            if(iconView != null) {
                if(icon != null) {
                    iconView.setImageDrawable(icon);
                    iconView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    iconView.setVisibility(8);
                    iconView.setImageDrawable((Drawable)null);
                }
            }

            boolean hasText = !TextUtils.isEmpty(text);
            if(textView != null) {
                if(hasText) {
                    textView.setText(text);
                    textView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText((CharSequence)null);
                }
            }

            if(iconView != null) {
                MarginLayoutParams lp = (MarginLayoutParams)iconView.getLayoutParams();
                int iconMargin = 0;
                if(hasText && iconView.getVisibility() == 0) {
                    iconMargin = CustomTabLayout.this.dpToPx(8);
                }

                if(CustomTabLayout.this.inlineLabel) {
                    if(iconMargin != MarginLayoutParamsCompat.getMarginEnd(lp)) {
                        MarginLayoutParamsCompat.setMarginEnd(lp, iconMargin);
                        lp.bottomMargin = 0;
                        iconView.setLayoutParams(lp);
                        iconView.requestLayout();
                    }
                } else if(iconMargin != lp.bottomMargin) {
                    lp.bottomMargin = iconMargin;
                    MarginLayoutParamsCompat.setMarginEnd(lp, 0);
                    iconView.setLayoutParams(lp);
                    iconView.requestLayout();
                }
            }

            CharSequence contentDesc = this.tab != null?this.tab.contentDesc:null;
            TooltipCompat.setTooltipText(this, hasText?null:contentDesc);
        }

        private int getContentWidth() {
            boolean initialized = false;
            int left = 0;
            int right = 0;
            View[] var4 = new View[]{this.textView, this.iconView, this.customView};
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                View view = var4[var6];
                if(view != null && view.getVisibility() == 0) {
                    left = initialized?Math.min(left, view.getLeft()):view.getLeft();
                    right = initialized?Math.max(right, view.getRight()):view.getRight();
                    initialized = true;
                }
            }

            return right - left;
        }

        public CustomTabLayout.Tab getTab() {
            return this.tab;
        }

        private float approximateLineWidth(Layout layout, int line, float textSize) {
            return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize());
        }
    }

    public static class Tab {
        public static final int INVALID_POSITION = -1;
        private Object tag;
        private Drawable icon;
        private CharSequence text;
        private CharSequence contentDesc;
        private int position = -1;
        private View customView;
        public CustomTabLayout parent;
        public CustomTabLayout.TabView view;

        public Tab() {
        }

        @Nullable
        public Object getTag() {
            return this.tag;
        }

        @NonNull
        public CustomTabLayout.Tab setTag(@Nullable Object tag) {
            this.tag = tag;
            return this;
        }

        @Nullable
        public View getCustomView() {
            return this.customView;
        }

        @NonNull
        public CustomTabLayout.Tab setCustomView(@Nullable View view) {
            this.customView = view;
            this.updateView();
            return this;
        }

        @NonNull
        public CustomTabLayout.Tab setCustomView(@LayoutRes int resId) {
            LayoutInflater inflater = LayoutInflater.from(this.view.getContext());

            return this.setCustomView(inflater.inflate(resId, this.view, false));
        }

        @Nullable
        public Drawable getIcon() {
            return this.icon;
        }

        public int getPosition() {
            return this.position;
        }

        void setPosition(int position) {
            this.position = position;
        }

        @Nullable
        public CharSequence getText() {
            return this.text;
        }

        @NonNull
        public CustomTabLayout.Tab setIcon(@Nullable Drawable icon) {
            this.icon = icon;
            this.updateView();
            return this;
        }

        @NonNull
        public CustomTabLayout.Tab setIcon(@DrawableRes int resId) {
            if(this.parent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            } else {
                return this.setIcon(AppCompatResources.getDrawable(this.parent.getContext(), resId));
            }
        }

        @NonNull
        public CustomTabLayout.Tab setText(@Nullable CharSequence text) {
            if(TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(text)) {
                this.view.setContentDescription(text);
            }

            this.text = text;
            this.updateView();
            return this;
        }

        @NonNull
        public CustomTabLayout.Tab setText(@StringRes int resId) {
            if(this.parent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            } else {
                return this.setText(this.parent.getResources().getText(resId));
            }
        }

        public void select() {
            if(this.parent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            } else {
                this.parent.selectTab(this);
            }
        }

        public boolean isSelected() {
            if(this.parent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            } else {
                return this.parent.getSelectedTabPosition() == this.position;
            }
        }

        @NonNull
        public CustomTabLayout.Tab setContentDescription(@StringRes int resId) {
            if(this.parent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            } else {
                return this.setContentDescription(this.parent.getResources().getText(resId));
            }
        }

        @NonNull
        public CustomTabLayout.Tab setContentDescription(@Nullable CharSequence contentDesc) {
            this.contentDesc = contentDesc;
            this.updateView();
            return this;
        }

        @Nullable
        public CharSequence getContentDescription() {
            return this.view == null?null:this.view.getContentDescription();
        }

        void updateView() {
            if(this.view != null) {
                this.view.update();
            }

        }

        void reset() {
            this.parent = null;
            this.view = null;
            this.tag = null;
            this.icon = null;
            this.text = null;
            this.contentDesc = null;
            this.position = -1;
            this.customView = null;
        }
    }

    public interface OnTabSelectedListener extends CustomTabLayout.BaseOnTabSelectedListener<CustomTabLayout.Tab> {
    }

    public interface BaseOnTabSelectedListener<T extends CustomTabLayout.Tab> {
        void onTabSelected(T var1);

        void onTabUnselected(T var1);

        void onTabReselected(T var1);
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface TabIndicatorGravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface TabGravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface Mode {
    }
}
