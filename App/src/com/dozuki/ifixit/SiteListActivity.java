package com.dozuki.ifixit;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SiteListActivity extends SherlockFragmentActivity {
   private static final String SITE_LIST = "SITE_LIST";

   private ListView mSiteRowView;
   private SiteListAdapter mSiteListAdapter;
   private ArrayList<Site> mSiteList;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      getSupportActionBar().setTitle("");
      setContentView(R.layout.site_list);

      mSiteRowView = (ListView)findViewById(R.id.siteListView);

      if (savedInstanceState != null) {
         mSiteList = (ArrayList<Site>)savedInstanceState.getSerializable(
          SITE_LIST);
      }

      if (mSiteList != null) {
         setSiteList(mSiteList);
      } else {
         getSiteList();
      }
   }

   @Override
   public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);

      outState.putSerializable(SITE_LIST, mSiteList);
   }

   public void setSiteList(ArrayList<Site> sites) {
      mSiteList = sites;
      mSiteListAdapter = new SiteListAdapter(sites);
      mSiteRowView.setAdapter(mSiteListAdapter);
   }

   private void getSiteList() {
      new APIHelper.APIResponder<ArrayList<Site>>() {
         public void execute() {
            APIHelper.getSites(SiteListActivity.this, this);
         }

         public void setResult(ArrayList<Site> result) {
            setSiteList(result);
         }

         public void error(AlertDialog dialog) {
            dialog.show();
         }
      }.execute();
   }

   private class SiteListAdapter extends BaseAdapter {
      private ArrayList<Site> mSites;

      public SiteListAdapter(ArrayList<Site> sites) {
         mSites = sites;
      }

      public int getCount() {
         return mSites.size();
      }

      public Object getItem(int position) {
         return mSites.get(position);
      }

      public long getItemId(int position) {
         return position;
      }

      public View getView(int position, View convertView, ViewGroup parent) {
         SiteRowView siteView = (SiteRowView)convertView;

         if (convertView == null) {
            siteView = new SiteRowView(SiteListActivity.this);
         }

         siteView.setSite(mSites.get(position));

         return siteView;
      }
   }
}