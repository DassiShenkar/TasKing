package com.tasking;

import android.os.AsyncTask;

public class UpdateFirebaseTask  {

    public static class UpdateTasks extends AsyncTask<Task, Void, String>{

        @Override
        protected void onPreExecute(){
            //invoked on ui thread before execution
        }

        @Override
        protected String doInBackground(Task... params) {
            //invoked on the background thread immediately after
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            // invoked on the UI thread after the background computation finishes
        }
    }

    public static class AddTasks extends AsyncTask<Task, Void, String>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Task... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result){

        }
    }

    public static class AddMember extends AsyncTask<Task, Void, String>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Task... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result){

        }
    }

    public static class GetMembers extends AsyncTask<Task, Void, String>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Task... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result){

        }
    }

    public static class GetTasks extends AsyncTask<Task, Void, String>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Task... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result){

        }
    }
}
