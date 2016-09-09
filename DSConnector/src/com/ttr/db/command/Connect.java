package com.ttr.db.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

public class Connect implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		System.out.println("test commande");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.out.println("test commande dispose");
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		System.out.println("test commande execute");
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		System.out.println("test commande isEnabled");
		return false;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		System.out.println("test commande isHandled");
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		System.out.println("test commande removeHandlerListener");
	}

}
