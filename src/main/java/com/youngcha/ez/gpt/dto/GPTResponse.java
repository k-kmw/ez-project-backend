package com.youngcha.ez.gpt.dto;

import java.util.List;

public class GPTResponse {

	private List<Choice> choices;

	public GPTResponse(List<Choice> choices) {
		super();
		this.choices = choices;
	}

	public GPTResponse() {
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	public static class Choice {
		// gpt 대화 인덱스 번호
		private int index;
		// 지피티로 부터 받은 메세지
		// 여기서 content는 유저의 prompt가 아닌 gpt로부터 받은 response
		private Message message;

		public Choice(int index, Message message) {
			super();
			this.index = index;
			this.message = message;
		}

		public Choice() {
			super();

		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;

		}
	}
}