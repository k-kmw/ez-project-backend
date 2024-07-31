package com.youngcha.ez.gpt.service;

import com.youngcha.ez.gpt.dto.GPT4oRequest;
import com.youngcha.ez.gpt.dto.GPTResponse;
import com.youngcha.ez.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GptService {

	@Value("${gpt4.model}")
	private String model4;

	@Value("${gpt.api.url}")
	private String apiUrl;

	private final FileUtil fileUtil;
	private final RestTemplate restTemplate;

	private final String prompt =
			"투자전문가의 입장으로 다음 레포트를 정리해줘 \n" +
			"파란색 글자는 그 다음 문단의 제목이야. \n" +
			"다음과 같은 형식으로 답변해줘. [문단의 제목, 문단의 요약] 과 같이 모든 문단을 포함해서 요약해줘. \n" +
			"[그림] 혹은 <표>가 문단의 제목인 경우는 무시해줘.\n"+
			"각 문단의 요약은 문단에 있는 내용만으로 요약해줘 \n" +
			"문단의 제목과 문단의 요약 사이에는 ,가 반드시 있어야 해. \n" +
			"문단과 문단 사이에는 다음과 같이 줄바꿈이 있어야해 \n" +
			"[문단의 제목, 문단의 요약]\n" +
			"[문단의 제목, 문단의 요약]\n";
	private final String prompt2 = "첫 번째 줄, 두 번째 줄을 [첫 번째 줄, 두 번째 줄] 과 같이 대괄호에 담아서 응답해.";
	private final String prompt3 = "이미지에 있는 기업 이름을 [기업 이름] 과 같이 대괄호에 담아서 응답해.";
	private final String prompt4 = "이미지에 있는 보고서 제목을 [보고서 제목] 과 같이 대괄호에 담아서 응답해.";

	public GptService(FileUtil fileUtil, RestTemplate restTemplate) {
		this.fileUtil = fileUtil;
		this.restTemplate = restTemplate;
	}

	// 이미지 그냥 선택
	public String getResponseByGPT4(){

		// 이미지 add 해서 붙이면 됨
		List<String>imgUrlList = new ArrayList<>();
		// 로컬에서 이미지들 들고오기 (다가져옴)
		List<String> allImageNames = fileUtil.getAllImageNames();
		// 페이지 순 정렬
		Collections.sort(allImageNames);
		for(int i=0;i<allImageNames.size();i++){
			if(allImageNames.get(i).contains("DS") || allImageNames.get(i).contains("title") || allImageNames.get(i).contains("targetPrice")){
				continue;
			}

			String image = fileUtil.getImageBase64(allImageNames.get(i));
			imgUrlList.add(new String("data:image/jpeg;base64," + image));
		}
		return getSubResult(imgUrlList,prompt);
	}

	String getSubResult(List<String> imgUrlList, String prompt){
		GPT4oRequest request = new GPT4oRequest(model4, prompt,imgUrlList, 0, 4092, 0, 0, 0);
		GPTResponse gptResponse = restTemplate.postForObject(apiUrl, request, GPTResponse.class);
		return gptResponse.getChoices().get(0).getMessage().getContent();
	}


	public String getTargetPrice(){
		List<String> image = getBase64EncodedImageByImageName("targetPrice");
		return getSubResult(image,prompt2);
	}

	public String getName(){
		List<String> image = getBase64EncodedImageByImageName("name");
		return getSubResult(image,prompt3);
	}

	public String getTitle(){
		List<String> image = getBase64EncodedImageByImageName("title");
		return getSubResult(image,prompt4);
	}

	public List<String> getBase64EncodedImageByImageName(String imageName){
		
		List<String>imgUrlList = new ArrayList<>();

		// 로컬에서 이미지들 들고오기 (다가져옴)
		List<String> allImageNames = fileUtil.getAllImageNames();
		for(int i=0;i<allImageNames.size();i++){
			if(allImageNames.get(i).contains(imageName)) {
				String image = fileUtil.getImageBase64(allImageNames.get(i));
				imgUrlList.add(new String("data:image/jpeg;base64," + image));
			}
		}
		return imgUrlList;
	}


}
