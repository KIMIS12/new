package test4;

import java.util.Vector;
import java.util.Arrays;
import java.util.Random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class testcode4_main {
	//빠르기
	public static final int fast =0;
	public static final int slow =1;
	//계절
	public static final int spring	=0;
	public static final int summer 	=1;
	public static final int fall 	=2;
	public static final int winter	=3;
	//시간대
	public static final int morning		=0;
	public static final int launch		=1;
	public static final int afternoon	=2;
	public static final int night		=3;
	public static final int dawn		=4;
	//날씨
	public static final int rain	=0;
	public static final int sunny	=1;
	public static final int cloudy	=2;
	public static final int thunder	=3;
	public static final int snow	=4;
	public static final int hot		=5;
	public static final int cold	=6;
	public static final int mid		=7;
	//기분
	public static final int excited			=0;
	public static final int delighted		=1;
	public static final int happy			=2;
	public static final int content			=3;
	public static final int relaxed			=4;
	public static final int calm			=5;
	public static final int tired			=6;
	public static final int bored			=7;
	public static final int depressed		=8;
	public static final int frustrated		=9;
	public static final int angry			=10;
	public static final int tense			=11;
	public static final int sleepy			=12;
	//분위기 "annoying", "angry", "nervous", "sad", "bored", "sleepy", "calm", "peaceful", "relaxed", "pleased", "happy", "excited"
	public static final int mood_annoying		=0;
	public static final int mood_angry			=1;
	public static final int mood_nervous		=2;
	public static final int mood_sad			=3;
	public static final int mood_bored			=4;
	public static final int mood_sleepy			=5;
	public static final int mood_calm			=6;
	public static final int mood_peaceful		=7;
	public static final int mood_relaxed		=8;
	public static final int mood_pleased		=9;
	public static final int mood_happy			=10;
	public static final int mood_excited		=11;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String roadaddr =null ;
		///////////////////////////////////////// 위도와 경도 정보로 주소를 알아낸다. -naver maps reverse geocoder 사용
		try {
			System.out.println("//////////////////////////지도 api 시도////////////////////////////"); 
			StringBuilder urlBuilder = new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc"); 
	        urlBuilder.append("?" + URLEncoder.encode("coords","UTF-8") + "=" + URLEncoder.encode("127.0199, 37.4845", "UTF-8")); 
	        urlBuilder.append("&" + URLEncoder.encode("orders","UTF-8") + "=" + URLEncoder.encode("roadaddr", "UTF-8"));
	        urlBuilder.append("&" + URLEncoder.encode("output","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
	        
	        
	        URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
			conn.setRequestMethod("GET");
			conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "99x7s0vnw0");
			conn.setRequestProperty("X-NCP-APIGW-API-KEY", "gvlTeHID3ZNb4SLXyLDsnCLF0cqlDYAlUIucGmau");
			
			//System.out.println("Response code: " + conn.getResponseCode());
			
			BufferedReader rd;
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			}
        
			StringBuilder sb = new StringBuilder();
			String line;
        
			while ((line = rd.readLine()) != null) {
				sb.append(line + "\n" );
			}
        
			rd.close();
        
			conn.disconnect();
			//System.out.println(sb);
			//System.out.println();
			
			String temp = sb.toString();
		       
			
	    	JSONParser jparser = new JSONParser();
	    	JSONObject jobject = (JSONObject)jparser.parse(temp);   
	    	//System.out.println("1. "+jobject);
	    	JSONArray jarray = (JSONArray) jobject.get("results");
	    	//System.out.println("2. "+jarray);
	    	
	    	JSONObject tempObject = (JSONObject)jarray.get(0);
	    	JSONObject tempObject2 = (JSONObject)tempObject.get("region");
			//System.out.println("3. "+tempObject2);
			
			JSONObject area = (JSONObject)tempObject2.get("area1");
			//System.out.println("4. "+area);
			
			String area1 = (String)area.get("name");
			//System.out.println("5. area1: "+area1);
			
			area = (JSONObject)tempObject2.get("area2");
			String area2 = (String)area.get("name");
			//System.out.println("6. area2: "+area2);
			
			area = (JSONObject)tempObject2.get("area3");
			String area3 = (String)area.get("name");
			//System.out.println("6. area3: "+area3);
			
			tempObject2 = (JSONObject)tempObject.get("land");
			//System.out.println("7. land: " +tempObject2);
			
			roadaddr = (String)tempObject2.get("name");
			//System.out.println("8. roadaddr: " + roadaddr);
			
		/////////////////////////////////////////////////////////////////////////////////	
		}catch (IOException r)
		{
			r.printStackTrace();
		}catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		
		
		
		//현재 감정과 감정에 따른 분위기가 저장될 벡터공간
		Vector<String> feeling = new Vector<String>();
		Vector<String> mood = new Vector<String>(); 
		
		//[설문조사 결과]주변환경(계절, 시간대, 날씨, 교통량-을 대변하는 빠르기)에 따른 감정이 저장된 공간
		Vector<String> season_feeling = new Vector<String>(); 
		season_feeling.add(spring, "happy");
		season_feeling.add(summer, "tired");
		season_feeling.add(fall, "calm");
		season_feeling.add(winter, "delighted");
		
		
		Vector<String>time_feeling = new Vector<String>();
		time_feeling.add(morning, "tired");
		time_feeling.add(launch, "relaxed");
		time_feeling.add(afternoon, "tired");
		time_feeling.add(night, "happy");
		time_feeling.add(dawn, "calm");
		
		Vector<String>weather_feeling = new Vector<String>();
		weather_feeling.add(rain, "calm");
		weather_feeling.add(sunny, "happy");
		weather_feeling.add(cloudy, "depressed");
		weather_feeling.add(thunder, "tense");
		weather_feeling.add(snow, "delighted");
		weather_feeling.add(hot, "tired");
		weather_feeling.add(cold, "calm");
		weather_feeling.add(mid, "relaxed");
		
		Vector<String>velocity_feeling = new Vector<String>();
		velocity_feeling.add(fast, "excited");
		velocity_feeling.add(slow, "tired");
		
		//주변환경 측정자료가 저장된 공간/////////////////////////////////////////////////////////////////////////////
		
		Date date = new Date();
		
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat hour = new SimpleDateFormat("HH");
		
		int mon = Integer.parseInt(month.format(date));
		int now = Integer.parseInt(hour.format(date));
		
		String season = null;
		boolean sleepy_m = false, sleepy_h=false;
		
		switch(mon) {
		case 12:
		case 1:
		case 2:
			season = "winter";
			break;
		case 4:
		case 5:sleepy_m = true;
		case 3:
			season = "spring";
			break;
		case 6:
		case 7:
		case 8:
		case 9:
			season = "summer";
			break;
			default: season = "fall";
		}
		
		if((now>=2 && now<=5)|| (now>=12 && now<=15)) sleepy_h=true;
		
		if(sleepy_m) feeling.add("sleepy");
		if(sleepy_h) feeling.add("sleepy");
		
		Vector<String> weather = new Vector<String>();
		String person = "angry";
		String v_a = "bored";
		
		testcode4_methods tools = new testcode4_methods();
		
		tools.get_weather_api(weather, 37.4845, 127.0199);  //위도latitude 경도longitude 
		
		int road_type = tools.get_traffic_info(roadaddr, 37.4845, 127.0199);
		
		double velocity = 70 ; //
		tools.get_traffic(feeling, velocity, velocity_feeling, road_type);
		
		//System.out.println("velocity: "+vel);
		
		//[설문조사 결과]감정과 그에 따른 분위기가 저장된 공간
		Vector<String> feeling_to_mood = new Vector<String>();
		feeling_to_mood.add(excited, "excited");
		feeling_to_mood.add(delighted, "relaxed");
		feeling_to_mood.add(happy, "happy");
		feeling_to_mood.add(content, "pleased");
		feeling_to_mood.add(relaxed, "calm");
		feeling_to_mood.add(calm, "sad");
		feeling_to_mood.add(tired, "excited");
		feeling_to_mood.add(bored, "excited");
		feeling_to_mood.add(depressed, "peaceful");
		feeling_to_mood.add(frustrated, "angry");
		feeling_to_mood.add(angry, "nervous");
		feeling_to_mood.add(tense, "calm");
		feeling_to_mood.add(sleepy, "excited");
		
		
		// 측정된 주변환경 정보를 감정정보로 변환한다. -> feeling에 저장
		tools.get_season(feeling, season, season_feeling);
		tools.get_time(feeling, now, time_feeling);
		tools.get_whether(feeling, weather, weather_feeling);
		feeling.add(person);
		feeling.add(v_a);
		
		//감정정보를 분위기 정보로 변환한다. -> mood에 저장
		tools.change_music(feeling, mood, feeling_to_mood);
		
		System.out.println(feeling);
		System.out.println(mood);
		
		Vector<Integer> music = new Vector<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0));
		
		
		// 분위기 인덱스별로 각 분위기의 가중치를 저장한다.
		for(int i=0; i<mood.size(); i++)
		{
			int index = tools.change(mood.get(i));
			music.set(index, music.get(index)+1);
		}
		
		System.out.println(music);
		
		String list[]= {"annoying", "angry", "nervous", "sad", "bored", "sleepy", "calm", "peaceful", "relaxed", "pleased", "happy", "excited"};
		int temps[] = {0,0,0,0,0,0,0,0,0,0,0,0};
		
		Random random = new Random();
		Vector<String> result_mood = new Vector<String>();
		
		//가중치 랜덤 함수
		for(int i=0 ; i<11; i++)
		{
			int n = random.nextInt(mood.size()+12);
			int sum=0;
			int j=0;
			
			while(sum<=n)
			{ 
				sum= sum+ music.get(j)+1;  //music.get(j)는 가중치
				j++;
			}
			result_mood.add(list[j-1]);
		}
		
		System.out.println(result_mood);
		
		for(int i=0; i<result_mood.size(); i++)
		{
			if(result_mood.get(i).compareTo("annoying")==0) temps[mood_annoying]++;
			else if(result_mood.get(i).compareTo("angry")==0) temps[mood_angry]++;
			else if(result_mood.get(i).compareTo("nervous")==0) temps[mood_nervous]++;
			else if(result_mood.get(i).compareTo("sad")==0) temps[mood_sad]++;
			else if(result_mood.get(i).compareTo("bored")==0) temps[mood_bored]++;
			else if(result_mood.get(i).compareTo("sleepy")==0) temps[mood_sleepy]++;
			else if(result_mood.get(i).compareTo("calm")==0) temps[mood_calm]++;
			else if(result_mood.get(i).compareTo("peaceful")==0) temps[mood_peaceful]++;
			else if(result_mood.get(i).compareTo("relaxed")==0) temps[mood_relaxed]++;
			else if(result_mood.get(i).compareTo("pleased")==0) temps[mood_pleased]++;
			else if(result_mood.get(i).compareTo("happy")==0) temps[mood_happy]++;
			else temps[mood_excited]++;
		}
		
		int n=1;
		
		for(int i=0; i<12; i++)
		{
			try{
				System.out.println("//////////////////////////last.fm api 시도////////////////////////////"); 
				StringBuilder urlBuilder = new StringBuilder("http://ws.audioscrobbler.com/2.0/"); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("method","UTF-8") + "="+ URLEncoder.encode("tag.gettoptracks", "UTF-8")); 
		        urlBuilder.append("&" + URLEncoder.encode("tag","UTF-8") + "=" + URLEncoder.encode(list[i], "UTF-8")); 
		        urlBuilder.append("&" + URLEncoder.encode("api_key","UTF-8") + "=6a08a55c41eb47e08a8eaf0905079779"); 
		        urlBuilder.append("&" + URLEncoder.encode("format","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
		        
		        URL url = new URL(urlBuilder.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        
				conn.setRequestMethod("GET");
				
				//System.out.println("Response code: " + conn.getResponseCode());
				
				BufferedReader rd;
				if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				} else {
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
				}
	        
				StringBuilder sb = new StringBuilder();
				String line;
	        
				while ((line = rd.readLine()) != null) {
					sb.append(line + "\n" );
				}
	        
				rd.close();
	        
				conn.disconnect();
				//System.out.println(sb);
				//System.out.println();
				
				String temp = sb.toString();
			       
				
		    	JSONParser jparser = new JSONParser();
		    	JSONObject jobject = (JSONObject)jparser.parse(temp);   
		    	//System.out.println("1. "+jobject);
		    	
		    	jobject = (JSONObject)jobject.get("tracks");   
		    	//System.out.println("2. "+jobject);
		    	
		    	JSONArray jarray = (JSONArray)jobject.get("track");
		    	//System.out.println("3. "+jarray);
		    	
		    	
		    	
		        for(int j=0; j<temps[i]; j++)
		        {
		        	JSONObject obj = (JSONObject)jarray.get(j);
		        	String name = (String)obj.get("name");
		        	String artist = (String)((JSONObject)obj.get("artist")).get("name");
		        	
		        	System.out.println(n +". "+name + " - "+ artist);
		        	n++;
		        	
		        }
		        
		        
			}catch (IOException r)
			{
				r.printStackTrace();
			}catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		
		
		 
	}

}
