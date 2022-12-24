package gallery.os.com.gallery.Dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.Model.MomentModel;
import gallery.os.com.gallery.R;


@SuppressLint("ValidFragment")
public class AddToMoment extends DialogFragment {
    Activity mactivity;
    Dialog mdialog;
    ArrayList<MomentModel> mtotleMoMent = new ArrayList<>();

    ListView listView;
    MomentDB momentDB;
    Button Btn_CreateMoment;
    OnSelectListener onSelectListener;


    @SuppressLint("ValidFragment")
    public AddToMoment(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }


    public interface OnSelectListener {
        void Select_Moment(View view, Dialog dialog, String s);

        void Create_New_Moment(View view, Dialog dialog);
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {

            mactivity = getActivity();
            try {
                //noinspection ConstantConditions
                mdialog = new Dialog(getActivity());
                mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //noinspection ConstantConditions
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.setContentView(R.layout.dialog_addtomoment);


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                listView = mdialog.findViewById(R.id.lv_listview);
                Btn_CreateMoment = mdialog.findViewById(R.id.btn_create);
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                momentDB = new MomentDB(mactivity);
                momentDB.open();
                try {
                    Cursor cursor = momentDB.getMomentlist();
                    cursor.moveToFirst();
                    do {
                        try {
                            mtotleMoMent.add(new MomentModel(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("momentname")), cursor.getString(cursor.getColumnIndex("momentThumbImage")), cursor.getLong(cursor.getColumnIndex("momentDate"))));
                        } catch (Exception ignored) {
                        }
                    } while (cursor.moveToNext());
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                CustomAdapter customAdapter = new CustomAdapter(mactivity, R.layout.addtomoment_adepter);
                listView.setAdapter(customAdapter);


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                Btn_CreateMoment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            onSelectListener.Create_New_Moment(view, mdialog);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return mdialog;
    }


    public class CustomAdapter extends ArrayAdapter<MomentModel> {
        CustomAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        private class ViewHolder {
            ImageView iv_more;
            TextView tv_Name, tv_Sub;
        }

        @Override
        public int getCount() {
            return mtotleMoMent.size();
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MomentModel momentModel = mtotleMoMent.get(position);
            ViewHolder viewHolder;
            try {
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    try {

                        LayoutInflater inflater = LayoutInflater.from(mactivity);
                        convertView = inflater.inflate(R.layout.addtomoment_adepter, parent, false);
                        viewHolder.tv_Name = (TextView) convertView.findViewById(R.id.tv_name);
                        viewHolder.tv_Sub = (TextView) convertView.findViewById(R.id.tv_count);
                        viewHolder.iv_more = (ImageView) convertView.findViewById(R.id.iv_more);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    convertView.setTag(viewHolder);
                } else {


                        viewHolder = (ViewHolder) convertView.getTag();



                }
                try {
                    viewHolder.tv_Name.setText(momentModel.getName());

                }catch (Exception e){e.printStackTrace();}


                try {
                    Cursor cursor = momentDB.getMomentlistdata(momentModel.getId());
                    cursor.moveToFirst();

                    viewHolder.tv_Sub.setText(cursor.getCount() + "  Photos");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    viewHolder.iv_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final PopupMenu popup = new PopupMenu(mactivity, view);
                            popup.getMenuInflater().inflate(R.menu.moment_option_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.rename:
                                            try {
                                                Rename rename = new Rename(new Rename.OnClickListener() {
                                                    @Override
                                                    public void Rename_Btn(View view, Dialog dialog, String s) {
                                                        try {
                                                            momentDB.updateMomentName(s, momentModel.getId());
                                                            MomentModel momentModel1 = new MomentModel(momentModel.getId(), s, momentModel.getPath(), momentModel.getDate());
                                                            mtotleMoMent.set(position, momentModel1);
                                                            notifyDataSetChanged();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void Cancel_Btn(View view, Dialog dialog) {

                                                    }
                                                });
                                                Bundle bundle = new Bundle();
                                                bundle.putString("filename", momentModel.getName());
                                                rename.setArguments(bundle);
                                                rename.show(getFragmentManager(), "");

                                            }catch (Exception e){e.printStackTrace();}
                                            //  momentDB.updateMomentName(editText.getText().toString(), momentID);
                                            break;
                                        case R.id.delete:
                                            try {
                                                momentDB.removeMoment(momentModel.getId());
                                                mtotleMoMent.remove(position);
                                                popup.dismiss();
                                                notifyDataSetChanged();
                                            }catch (Exception e){e.printStackTrace();}



                                            break;
                                    }
                                    return true;
                                }
                            });
                            popup.show();
                        }
                    });
                }catch (Exception e){e.printStackTrace();}
                try {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                                onSelectListener.Select_Moment(view, mdialog, momentModel.getId());

                            }catch (Exception e){e.printStackTrace();}

       }
                    });
                }catch (Exception e){e.printStackTrace();}
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
