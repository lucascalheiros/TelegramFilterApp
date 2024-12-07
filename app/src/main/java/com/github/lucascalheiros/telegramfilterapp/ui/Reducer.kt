package com.github.lucascalheiros.telegramfilterapp.ui

/**
 * The reduces will be responsible to hold the state change logic and to perform this logic defined
 * through a set of actions. In the MVI architecture we will have the following event flow:
 * User event -> Intent -> Middleware (side effects) -> Action -> State Update
 * The intent provide a set of actions that the user can perform, these actions are dispatched to a
 * middleware which will perform the side effects required, and trigger 0 or more Actions that perform
 * a state update.
 * Intent and Actions are different in the sense of their objectives, for example:
 *  A Intent to RefreshPage is triggered by the user, this Intent will trigger the async operation
 *  to fetch some data list, before doing this operation it could trigger an Action to enable a
 *  DataLoading state, and after its conclusion it could trigger an Action to UpdateDataList
 */
interface Reducer<State, Action> {
    /**
     * @param state The current state of data which will be mapped into a new state
     * @param action A logical set of operations that will cause a state change
     * @return A new state based on the logic performed by the action
     */
    fun reduce(state: State, action: Action): State
}