package org.dsf.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class BaseServerHandler extends ChannelHandlerAdapter {
	
	private Map<Channel, Object> channelMap;
	
	public BaseServerHandler() {
		channelMap = new HashMap<Channel, Object>();
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  
		ctx.channel();
		
		StringBuilder sb = (StringBuilder) getOrAddChannel(ctx.channel());
		
		
        System.out.println("BaseServerInHandler.channelRead");  
        ByteBuf result = (ByteBuf) msg;  
        byte[] result1 = new byte[result.readableBytes()];  
        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中  
        result.readBytes(result1);  
        String resultStr = new String(result1);  
        sb.append(resultStr);
        // 接收并打印客户端的信息  
        System.out.println("Client said:" + sb.toString());  
        // 释放资源，这行很关键  
        result.release();  
  
        // 向客户端发送消息  
        String response = "I am ok!";  
        // 在当前场景下，发送的数据必须转换成ByteBuf数组  
        ByteBuf encoded = ctx.alloc().buffer(4 * response.length());  
        encoded.writeBytes(response.getBytes());  
        ctx.write(encoded);  
        ctx.flush();  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        ctx.flush();  
    }  
    
    private Object getOrAddChannel(Channel channel) {
    	Object obj = channelMap.get(channel);
    	if (obj == null) {
    		obj = new StringBuilder();
    		channelMap.put(channel, obj);
    	}
    	return obj;
    }
}
