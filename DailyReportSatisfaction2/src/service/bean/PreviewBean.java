package service.bean;

import java.io.Serializable;
import java.util.List;

import domain.Draft;
import domain.valueo.Destination;
import domain.valueo.Mailaddress;
import domain.valueo.Sender;
import domain.valueo.Title;

public class PreviewBean implements Serializable{

	private Sender sender;					// 送信者
	private Mailaddress senderAddress;		//送信者アドレス
	private Destination destination;		// 宛先
	private Title title;					// 件名
	private List<Draft> draft;						// 本文
	private String text;					//表示テキスト
	private String sendText;			//送信テキスト


	public Mailaddress getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(Mailaddress senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSendText() {
		return sendText;
	}

	public void setSendText(String sendText) {
		this.sendText = sendText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Draft> getDraft() {
		return draft;
	}

	public void setDraft(List<Draft> draft) {
		this.draft = draft;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

}
