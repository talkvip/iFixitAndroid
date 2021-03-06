package com.dozuki.ifixit.util;

import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.widget.ImageView;
import com.dozuki.ifixit.MainApplication;
import com.dozuki.ifixit.model.dozuki.Site;

public class Utils {
   public static void stripImageView(ImageView view) {
      if ( view.getDrawable() instanceof BitmapDrawable) {
         ((BitmapDrawable)view.getDrawable()).getBitmap().recycle();
      }

      if (view.getDrawable() != null)
         view.getDrawable().setCallback(null);

      view.setImageDrawable(null);
      view.getResources().flushLayoutCache();
      view.destroyDrawingCache();
   }

   /**
    * Removes relative link hrefs
    *
    * @param spantext (from Html.fromhtml())
    * @return spanned with fixed links
    */
   public static Spanned correctLinkPaths(Spanned spantext) {
      Object[] spans = spantext.getSpans(0, spantext.length(), Object.class);
      for (Object span : spans) {
         int start = spantext.getSpanStart(span);
         int end = spantext.getSpanEnd(span);
         int flags = spantext.getSpanFlags(span);

         Site site = MainApplication.get().getSite();

         if (span instanceof URLSpan) {
            URLSpan urlSpan = (URLSpan) span;
            if (!urlSpan.getURL().startsWith("http")) {
               if (urlSpan.getURL().startsWith("/")) {
                  urlSpan = new URLSpan("http://" + site.mDomain + urlSpan.getURL());
               } else {
                  urlSpan = new URLSpan("http://" + site.mDomain + "/" + urlSpan.getURL());
               }
            }
            ((Spannable) spantext).removeSpan(span);
            ((Spannable) spantext).setSpan(urlSpan, start, end, flags);
         }
      }

      return spantext;
   }
}
